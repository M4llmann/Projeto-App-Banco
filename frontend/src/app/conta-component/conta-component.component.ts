import { Component, OnInit } from '@angular/core';
import { ContaService } from '../services/conta.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ContaDTO } from '../conta.model';
import { TransacaoDTO } from '../transacao.model';
import { Router } from '@angular/router';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { AuthInterceptor } from '../auth.interceptor';

@Component({
  standalone: true,
  selector: 'app-conta',
  templateUrl: './conta-component.component.html',
  styleUrls: ['./conta-component.component.css'],
  imports: [CommonModule, FormsModule, HttpClientModule,
  ],
  providers: [
    ContaService,
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ]
})
export class ContaComponent implements OnInit {
  contas: ContaDTO[] = [];
  contaSelecionada: ContaDTO | null = null;
  nomeTitular: string = '';
  valor: number = 0;
  mostrarExtrato: boolean = false;
  extrato: TransacaoDTO[] = [];
  erro: boolean = false;
  loading: boolean = false;
  loadingCriarConta: boolean = false;
  loadingTransacao: boolean = false;
  loadingExtrato: boolean = false;
  tipoTransacao: string = '';
  mensagemErro: string = '';
  mensagemSucesso: string = '';
  extratoCarregado: boolean = false;

  constructor(private contaService: ContaService, private router: Router) { }

  ngOnInit(): void {
    // Verifica se o token de autenticação está presente
    const token = localStorage.getItem('token');
    if (!token) {
      this.router.navigate(['/login']);
      return;
    }

    this.listarContas();
  }

  listarContas(): void {
    const idUsuario = localStorage.getItem('idUsuario');
    if (!idUsuario) {
      this.router.navigate(['']);
      return;
    }

    this.loading = true;
    this.erro = false;
    this.mensagemErro = '';

    this.contaService.listarContasPorUsuario().subscribe({
      next: (dados: ContaDTO | ContaDTO[]) => {
        this.loading = false;
        
        if (Array.isArray(dados)) {
          this.contas = dados;
        } else {
          this.contas = [dados];
        }

        this.erro = false;
        if (this.contas.length > 0) {
          this.selecionarConta(this.contas[0]);
        }
      },
      error: (err) => {
        this.loading = false;
        this.erro = true;
        const errorMessage = err.error?.message || 'Erro ao carregar contas.';
        this.mensagemErro = errorMessage;
      },
    });
  }

  selecionarConta(conta: ContaDTO): void {
    this.contaSelecionada = conta;
    this.mostrarExtrato = false;
    this.extrato = [];
    this.extratoCarregado = false;
    this.mensagemErro = '';
    this.mensagemSucesso = '';
  }

  criarConta(): void {
    const idUsuario = localStorage.getItem('idUsuario');
    if (!idUsuario) {
      this.mensagemErro = 'Usuário não autenticado!';
      return;
    }

    if (!this.nomeTitular.trim()) {
      this.mensagemErro = 'Informe o nome do titular para criar a conta.';
      return;
    }

    this.loadingCriarConta = true;
    this.mensagemErro = '';
    this.mensagemSucesso = '';

    this.contaService.criarConta({ nomeTitular: this.nomeTitular }, +idUsuario).subscribe({
      next: (novaConta: ContaDTO) => {
        this.loadingCriarConta = false;
        this.mensagemSucesso = 'Conta criada com sucesso!';
        if (novaConta) {
          this.contas.push(novaConta);
          this.selecionarConta(novaConta);
          this.nomeTitular = '';
          setTimeout(() => {
            this.mensagemSucesso = '';
          }, 3000);
        }
      },
      error: (err) => {
        this.loadingCriarConta = false;
        const errorMessage = err.error?.message || 'Erro ao criar a conta. Tente novamente.';
        this.mensagemErro = errorMessage;
      }
    });
  }

  realizarDeposito(): void {
    if (!this.contaSelecionada || this.valor <= 0) {
      this.mensagemErro = 'Informe um valor válido para depósito.';
      return;
    }

    this.loadingTransacao = true;
    this.tipoTransacao = 'deposito';
    this.mensagemErro = '';
    this.mensagemSucesso = '';

    this.contaService.realizarDeposito(this.contaSelecionada.idConta, this.valor).subscribe({
      next: (contaAtualizada) => {
        this.loadingTransacao = false;
        this.mensagemSucesso = 'Depósito realizado com sucesso!';
        this.contaSelecionada = contaAtualizada;
        this.valor = 0;
        setTimeout(() => {
          this.mensagemSucesso = '';
        }, 3000);
      },
      error: (err) => {
        this.loadingTransacao = false;
        const errorMessage = err.error?.message || 'Erro ao realizar depósito. Tente novamente.';
        this.mensagemErro = errorMessage;
      },
    });
  }

  realizarSaque(): void {
    if (!this.contaSelecionada || this.valor <= 0) {
      this.mensagemErro = 'Informe um valor válido para saque.';
      return;
    }

    this.loadingTransacao = true;
    this.tipoTransacao = 'saque';
    this.mensagemErro = '';
    this.mensagemSucesso = '';

    this.contaService.realizarSaque(this.contaSelecionada.idConta, this.valor).subscribe({
      next: (contaAtualizada) => {
        this.loadingTransacao = false;
        this.mensagemSucesso = 'Saque realizado com sucesso!';
        this.contaSelecionada = contaAtualizada;
        this.valor = 0;
        setTimeout(() => {
          this.mensagemSucesso = '';
        }, 3000);
      },
      error: (err) => {
        this.loadingTransacao = false;
        const errorMessage = err.error?.message || 'Erro ao realizar saque. Verifique seu saldo.';
        this.mensagemErro = errorMessage;
      },
    });
  }


  carregarExtrato(): void {
    if (!this.contaSelecionada) {
      this.mensagemErro = 'Selecione uma conta para visualizar o extrato.';
      return;
    }

    this.loadingExtrato = true;
    this.mensagemErro = '';

    this.contaService.getExtrato(this.contaSelecionada.idConta).subscribe({
      next: (extrato) => {
        this.loadingExtrato = false;
        this.extrato = extrato;
        this.extratoCarregado = true;
      },
      error: (err) => {
        this.loadingExtrato = false;
        const errorMessage = err.error?.message || 'Erro ao carregar o extrato.';
        this.mensagemErro = errorMessage;
      },
    });
  }

  carregarDetalhesConta(): void {
    if (!this.contaSelecionada) {
      return;
    }

    this.contaService.obterDetalhesConta(this.contaSelecionada.idConta).subscribe({
      next: (contaAtualizada) => {
        this.contaSelecionada = contaAtualizada;
      },
      error: (err) => {
        console.error('Erro ao carregar detalhes da conta:', err);
        alert('Erro ao carregar detalhes da conta.');
      },
    });
  }
  sair(): void {
    localStorage.removeItem('idUsuario');
    localStorage.removeItem('token');
    this.router.navigate(['']);
  }

}
