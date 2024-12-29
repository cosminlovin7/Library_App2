import {Component, effect, Injectable, OnInit, signal} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {LoadingSpinnerComponent} from './shared/components/loading-spinner/loading-spinner.component';
import {MatProgressBar} from '@angular/material/progress-bar';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, LoadingSpinnerComponent, MatProgressBar],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})

@Injectable({providedIn: 'root'})
export class AppComponent {

  constructor(private http: HttpClient) {
    effect(() => {
      const spinner = document.getElementById('loading-spinner');
      if (spinner) {
        if (isLoading()) {
          spinner.style.display = 'block'; // Show spinner
        } else {
          spinner.style.display = 'none'; // Hide spinner
        }
      }
    });
  }

}

export const isLoading = signal(false);
