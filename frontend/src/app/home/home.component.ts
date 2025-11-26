import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { UsuarioService } from '../services/usuario.service';
import { Usuario } from '../usuario.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  imports: [FormsModule, CommonModule],
})
export class HomeComponent {
  email: string = '';
  senha: string = '';
  erroLogin: string = '';
  erroCadastro: string = '';
  loading: boolean = false;
  loadingLogin: boolean = false;

  constructor(private usuarioService: UsuarioService, private router: Router) { }

  // Método para cadastrar usuário
  cadastrar(): void {
    if (!this.email || !this.senha) {
      this.erroCadastro = 'Preencha todos os campos!';
      return;
    }

    if (this.senha.length < 6) {
      this.erroCadastro = 'A senha deve ter no mínimo 6 caracteres';
      return;
    }

    this.loading = true;
    this.erroCadastro = '';
    this.erroLogin = '';

    const usuarioDTO = {
      email: this.email,
      senha: this.senha,
    };

    this.usuarioService.criarUsuario(usuarioDTO).subscribe({
      next: (usuario: Usuario) => {
        this.loading = false;
        if (usuario.idUsuario) {
          localStorage.setItem('idUsuario', usuario.idUsuario.toString());
          this.router.navigate(['/conta']);
        } else {
          this.erroCadastro = 'Erro: idUsuario não encontrado na resposta';
        }
      },
      error: (err) => {
        this.loading = false;
        const errorMessage = err.error?.message || err.message || 'Erro ao realizar o cadastro. Tente novamente.';
        this.erroCadastro = errorMessage;
      },
    });
  }

  // Método para login de usuário
  login(): void {
    if (!this.email || !this.senha) {
      this.erroLogin = 'Preencha todos os campos!';
      return;
    }

    this.loadingLogin = true;
    this.erroLogin = '';
    this.erroCadastro = '';

    const usuarioDTO = {
      email: this.email,
      senha: this.senha,
    };

    this.usuarioService.login(usuarioDTO).subscribe({
      next: (response: any) => {
        this.loadingLogin = false;
        const token = response.token;
        const usuario = response.usuario;
        if (token) {
          localStorage.setItem('token', token);
          localStorage.setItem('idUsuario', usuario.idUsuario.toString());
          this.router.navigate(['/conta']);
        } else {
          this.erroLogin = 'Erro: Token não encontrado na resposta';
        }
      },
      error: (err) => {
        this.loadingLogin = false;
        const errorMessage = err.error?.message || 'E-mail ou senha incorretos.';
        this.erroLogin = errorMessage;
      },
    });
  }
}