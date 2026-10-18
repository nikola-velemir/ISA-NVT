import { Component, computed, inject, input } from "@angular/core";
import { DomSanitizer } from "@angular/platform-browser";

@Component({
  imports: [],
  selector: "app-temperature-grafana.component",
  styleUrl: "./temperature-grafana.component.css",
  templateUrl: "./temperature-grafana.component.html",
})
export class TemperatureGrafanaComponent { private sanitizer = inject(DomSanitizer);

  /** Flux range start from the dropdown, e.g. "-1h" */
  span = input.required<string>();

  // Replace with your dashboard uid, slug and panel id (ideally from environment config)
  private readonly baseUrl = 'http://localhost:3000/d-solo/YOUR_DASHBOARD_UID/temperatures';
  private readonly panelId = '1';

  readonly src = computed(() => {
    const params = new URLSearchParams({
      orgId: '1',
      panelId: this.panelId,
      from: `now${this.span()}`, // "-1h" -> "now-1h"
      to: 'now',
      refresh: '5s',
      theme: 'light',
    });
    // Safe: the base URL is a constant and span comes from a fixed list
    return this.sanitizer.bypassSecurityTrustResourceUrl(`${this.baseUrl}?${params}`);
  });
}
