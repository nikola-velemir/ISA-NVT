import { Component, DestroyRef, inject, signal } from '@angular/core';
import { Subscription, switchMap, tap } from 'rxjs';
import { ImageService } from '../../../../../core/services/image.service';

/** Tab 1: upload an image, and the server's response is downloaded straight away. */
@Component({
  standalone: true,
  selector: 'app-what-not-to-do',
  templateUrl: './what-not-to-do-component.html',
})
export class WhatNotToDoComponent {
  private readonly service = inject(ImageService);
  private sub?: Subscription;

  readonly dragging = signal(false);
  readonly status = signal<'idle' | 'uploading' | 'done' | 'error'>('idle');
  readonly error = signal('');
  readonly fileName = signal('');

  constructor() {
    inject(DestroyRef).onDestroy(() => this.sub?.unsubscribe());
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.handleFile(input.files?.[0]);
    input.value = ''; // allow re-selecting the same file
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
    this.dragging.set(true);
  }

  onDrop(event: DragEvent): void {
    event.preventDefault();
    this.dragging.set(false);
    this.handleFile(event.dataTransfer?.files?.[0]);
  }

  private handleFile(file?: File | null): void {
    if (!file) return;

    if (!file.type.startsWith('image/')) {
      this.error.set('Only image files are supported.');
      return;
    }

    this.error.set('');
    this.send(file);
  }

  private send(file: File): void {
    // A new file replaces any upload still in progress
    this.sub?.unsubscribe();
    this.status.set('uploading');

    // upload() returns void, so the key is assumed to be the file name
    this.sub = this.service
      .upload({ file })
      .pipe(
         switchMap((s) => this.service.download(s)))
      .subscribe({
        next: (res) => {
          this.saveBlob(res.file, file.name);
          this.fileName.set(file.name);
          this.status.set('done');
        },
        error: (e) => {
          this.status.set('error');
          this.error.set('Upload failed. Try again.');
        },
      });
  }

  private saveBlob(blob: Blob, name: string): void {
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = name;
    document.body.appendChild(a);
    a.click();
    a.remove();
    URL.revokeObjectURL(url);
  }
}