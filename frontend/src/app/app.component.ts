import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { CommonModule } from '@angular/common';

interface ShortenRequest {
  url: string;
}

interface ShortenResponse {
  shortcode: string;
}


@Component({
  selector: 'app-root',
  imports: [CommonModule ,ReactiveFormsModule],
  templateUrl: './app.component.html',
  standalone: true,
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'URL Shortener';

  // Base URL de tu API
  private readonly API_BASE = 'http://localhost:8081/api/v1';

  // Forms
  shortenForm!: FormGroup;
  getUrlForm!: FormGroup;
  getStatsForm!: FormGroup;
  updateUrlForm!: FormGroup;
  deleteUrlForm!: FormGroup;

  // Results
  shortenResult: ShortenResponse | null = null;
  urlResult: string | null = null;
  statsResult: number | null = null;

  // Loading states
  isShortening = false;
  isGettingUrl = false;
  isGettingStats = false;
  isUpdating = false;
  isDeleting = false;

  // Messages
  successMessage = '';
  errorMessage = '';

  constructor(
    private http: HttpClient,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.initializeForms();
  }

  private initializeForms() {
    this.shortenForm = this.fb.group({
      url: ['', [Validators.required, Validators.pattern(/^https?:\/\/.+/)]],
      customShortcode: ['']
    });

    this.getUrlForm = this.fb.group({
      shortcode: ['', Validators.required]
    });

    this.getStatsForm = this.fb.group({
      shortcode: ['', Validators.required]
    });

    this.updateUrlForm = this.fb.group({
      shortcode: ['', Validators.required],
      newUrl: ['', [Validators.required, Validators.pattern(/^https?:\/\/.+/)]]
    });

    this.deleteUrlForm = this.fb.group({
      shortcode: ['', Validators.required]
    });
  }

  private clearMessages() {
    this.successMessage = '';
    this.errorMessage = '';
  }

  private showSuccess(message: string) {
    this.clearMessages();
    this.successMessage = message;
    setTimeout(() => this.successMessage = '', 5000);
  }

  private showError(message: string) {
    this.clearMessages();
    this.errorMessage = message;
    setTimeout(() => this.errorMessage = '', 5000);
  }

  // Crear shortcode para URL
  shortenUrl() {
    if (this.shortenForm.invalid) return;

    this.isShortening = true;
    this.shortenResult = null;

    const request: ShortenRequest = {
      url: this.shortenForm.value.url
    };


    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    this.http.post<ShortenResponse>(`${this.API_BASE}/write/shorten`, request, { headers })
      .subscribe({
        next: (response) => {
          this.shortenResult = response;
          this.showSuccess('URL acortada exitosamente!');
          this.isShortening = false;
        },
        error: (error) => {
          console.error('Error shortening URL:', error);
          this.showError(error.error?.message || 'Error al acortar la URL');
          this.isShortening = false;
        }
      });
  }

  // Obtener URL por shortcode
  getUrl() {
    if (this.getUrlForm.invalid) return;

    this.isGettingUrl = true;
    this.urlResult = null;

    const shortcode = this.getUrlForm.value.shortcode;

    this.http.get(`${this.API_BASE}/read/shorten/${shortcode}`, { responseType: 'text' })
      .subscribe({
        next: (response) => {
          this.urlResult = response;
          this.showSuccess('URL encontrada!');
          this.isGettingUrl = false;
        },
        error: (error) => {
          console.error('Error getting URL:', error);
          this.showError('Shortcode no encontrado');
          this.isGettingUrl = false;
        }
      });
  }

  // Obtener estadísticas por shortcode
  getStats() {
    if (this.getStatsForm.invalid) return;

    this.isGettingStats = true;
    this.statsResult = null;

    const shortcode = this.getStatsForm.value.shortcode;

    this.http.get<number>(`${this.API_BASE}/read/shorten/${shortcode}/stats`)
      .subscribe({
        next: (response) => {
          this.statsResult = response;
          this.showSuccess('Estadísticas obtenidas!');
          this.isGettingStats = false;
        },
        error: (error) => {
          console.error('Error getting stats:', error);
          this.showError('No se pudieron obtener las estadísticas');
          this.isGettingStats = false;
        }
      });
  }

  // Actualizar URL por shortcode
  updateUrl() {
    if (this.updateUrlForm.invalid) return;

    this.isUpdating = true;

    const shortcode = this.updateUrlForm.value.shortcode;
    const newUrl = this.updateUrlForm.value.newUrl;

    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    this.http.put(`${this.API_BASE}/write/shorten/${shortcode}`, { url: newUrl }, { headers })
      .subscribe({
        next: () => {
          this.showSuccess('URL actualizada exitosamente!');
          this.updateUrlForm.reset();
          this.isUpdating = false;
        },
        error: (error) => {
          console.error('Error updating URL:', error);
          this.showError('Error al actualizar la URL');
          this.isUpdating = false;
        }
      });
  }

  // Eliminar URL por shortcode
  deleteUrl() {
    if (this.deleteUrlForm.invalid) return;

    if (!confirm('¿Estás seguro de que quieres eliminar este shortcode?')) {
      return;
    }

    this.isDeleting = true;

    const shortcode = this.deleteUrlForm.value.shortcode;

    this.http.delete(`${this.API_BASE}/write/shorten/${shortcode}`)
      .subscribe({
        next: () => {
          this.showSuccess('Shortcode eliminado exitosamente!');
          this.deleteUrlForm.reset();
          this.isDeleting = false;
        },
        error: (error) => {
          console.error('Error deleting URL:', error);
          this.showError('Error al eliminar el shortcode');
          this.isDeleting = false;
        }
      });
  }

  // Copiar al portapapeles
  copyToClipboard(text: string | null) {
    if(text) {
      navigator.clipboard.writeText(text).then(() => {
        this.showSuccess('Copiado al portapapeles!');
      });
    }else{
     console.warn("No hay nada que copiar al portapapeles!");
    }

  }

  // Abrir URL en nueva pestaña
  openUrl(url: string | null) {
    if (url) {
      window.open(url, '_blank');
    } else {
      console.warn("URL es nulo");
    }
  }
}
