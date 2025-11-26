import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable, throwError } from 'rxjs';
import { ContaDTO } from '../conta.model';

@Injectable({
  providedIn: 'root',
})
export class ContaService {
  private baseUrl: string = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  criarConta(dadosConta: { nomeTitular: string }, idUsuario: number): Observable<ContaDTO> {
    return this.http.post<ContaDTO>(`${this.baseUrl}/contas/${idUsuario}`, dadosConta);
  }

  listarContasPorUsuario(): Observable<ContaDTO[]> {
    const idUsuario = localStorage.getItem('idUsuario');
    if (!idUsuario) {
      return throwError(() => new Error('Usuário não encontrado no localStorage'));
    }

    return this.http.get<ContaDTO[]>(`${this.baseUrl}/contas/usuario/${idUsuario}`);
  }

  getSaldo(idConta: number): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/contas/${idConta}/saldo`);
  }

  realizarDeposito(idConta: number, valor: number): Observable<ContaDTO> {
    const formattedValue = valor.toFixed(2);
    const url = `${this.baseUrl}/contas/${idConta}/deposito?valor=${formattedValue}`;
    return this.http.post<ContaDTO>(url, {});
  }

  realizarSaque(idConta: number, valor: number): Observable<ContaDTO> {
    return this.http.post<ContaDTO>(`${this.baseUrl}/contas/${idConta}/saque`, null, {
      params: { valor: valor.toString() }
    });
  }

  getExtrato(idConta: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/contas/${idConta}/extrato`);
  }

  obterDetalhesConta(idConta: number): Observable<ContaDTO> {
    return this.http.get<ContaDTO>(`${this.baseUrl}/contas/${idConta}`);
  }
}
