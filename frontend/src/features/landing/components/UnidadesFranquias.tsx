'use client';

import { useEffect, useRef } from 'react';

const CAPITAIS = [
  { estado: 'AC', label: 'Rio Branco', lat: -9.9794, lng: -67.8245 },
  { estado: 'AL', label: 'Maceió', lat: -9.6476, lng: -35.7353 },
  { estado: 'AM', label: 'Manaus', lat: -3.1190, lng: -60.0217 },
  { estado: 'AP', label: 'Macapá', lat: 0.3551, lng: -51.1145 },
  { estado: 'BA', label: 'Salvador', lat: -12.9822, lng: -38.5104 },
  { estado: 'CE', label: 'Fortaleza', lat: -3.7315, lng: -38.5275 },
  { estado: 'DF', label: 'Brasília', lat: -15.8267, lng: -47.8711 },
  { estado: 'ES', label: 'Vitória', lat: -20.2977, lng: -40.2925 },
  { estado: 'GO', label: 'Goiânia', lat: -15.8942, lng: -48.9822 },
  { estado: 'MA', label: 'São Luís', lat: -2.9140, lng: -44.3015 },
  { estado: 'MG', label: 'Belo Horizonte', lat: -19.9191, lng: -43.9386 },
  { estado: 'MS', label: 'Campo Grande', lat: -20.4697, lng: -54.6201 },
  { estado: 'MT', label: 'Cuiabá', lat: -15.5939, lng: -56.0912 },
  { estado: 'PA', label: 'Belém', lat: -1.4554, lng: -48.4915 },
  { estado: 'PB', label: 'João Pessoa', lat: -7.1196, lng: -34.8644 },
  { estado: 'PE', label: 'Recife', lat: -8.0476, lng: -34.8770 },
  { estado: 'PI', label: 'Teresina', lat: -5.0891, lng: -42.8019 },
  { estado: 'PR', label: 'Curitiba', lat: -25.4284, lng: -49.2733 },
  { estado: 'RJ', label: 'Rio de Janeiro', lat: -22.9068, lng: -43.1729 },
  { estado: 'RN', label: 'Natal', lat: -5.7921, lng: -35.2094 },
  { estado: 'RO', label: 'Porto Velho', lat: -8.7619, lng: -63.9039 },
  { estado: 'RR', label: 'Boa Vista', lat: 2.8235, lng: -60.6758 },
  { estado: 'RS', label: 'Porto Alegre', lat: -30.0346, lng: -51.2177 },
  { estado: 'SC', label: 'Florianópolis', lat: -27.5954, lng: -48.5480 },
  { estado: 'SE', label: 'Aracaju', lat: -10.9111, lng: -37.0705 },
  { estado: 'SP', label: 'São Paulo', lat: -23.5505, lng: -46.6333 },
  { estado: 'TO', label: 'Palmas', lat: -10.2105, lng: -48.3281 },
];

export function UnidadesFranquias() {
  const mapContainer = useRef<HTMLDivElement>(null);
  const map = useRef<any>(null);

  useEffect(() => {
    if (!mapContainer.current || map.current) return;

    const initMap = async () => {
      const L = (await import('leaflet')).default;
      await import('leaflet/dist/leaflet.css');

      map.current = L.map(mapContainer.current as HTMLElement, {
        attributionControl: false,
      }).setView([-14, -51.5], 4);

      L.tileLayer('https://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}{r}.png', {
        maxZoom: 19,
      }).addTo(map.current);

      const style = document.createElement('style');
      style.textContent = `
        .leaflet-container {
          background-color: #e0e0e0 !important;
          font-family: var(--font-primary) !important;
        }
        .leaflet-tile-pane {
          filter: grayscale(100%) brightness(0.85) contrast(1.2);
        }
        .leaflet-control-zoom {
          border: 1px solid #999999 !important;
          background-color: #f5f5f5 !important;
        }
        .leaflet-control-zoom-in,
        .leaflet-control-zoom-out {
          background-color: #e8e8e8 !important;
          color: #005eff !important;
          border-bottom: 1px solid #999999 !important;
          font-weight: 600;
        }
        .leaflet-control-zoom-in:hover,
        .leaflet-control-zoom-out:hover {
          background-color: #d0d0d0 !important;
        }
        .leaflet-popup-content {
          color: #0a2540;
          font-family: var(--font-primary);
        }
        .leaflet-popup-content strong {
          color: #005eff;
          font-weight: 600;
        }
        .leaflet-popup-tip {
          background-color: #ffffff !important;
        }
        .leaflet-popup-content-wrapper {
          background-color: #ffffff !important;
          border-radius: 8px !important;
          box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2) !important;
          border: 1px solid #999999 !important;
        }
      `;
      document.head.appendChild(style);

      CAPITAIS.forEach(({ estado, label, lat, lng }) => {
        const marker = L.circleMarker([lat, lng], {
          radius: 8,
          fillColor: '#005eff',
          color: '#d3d3d3',
          weight: 2,
          opacity: 1,
          fillOpacity: 0.8,
        }).addTo(map.current);

        marker.bindPopup(`<strong>${estado}</strong><br>${label}`);

        const pulse = L.circleMarker([lat, lng], {
          radius: 12,
          fillColor: '#005eff',
          color: 'rgba(0, 94, 255, 0.4)',
          weight: 1,
          opacity: 0.4,
          fillOpacity: 0.15,
        }).addTo(map.current);

        let pulseRadius = 12;
        let expanding = true;

        setInterval(() => {
          if (expanding) {
            pulseRadius += 1;
            if (pulseRadius > 25) expanding = false;
          } else {
            pulseRadius -= 1;
            if (pulseRadius < 12) expanding = true;
          }
          pulse.setRadius(pulseRadius);
        }, 100);
      });
    };

    initMap();

    return () => {
      if (map.current) {
        map.current.remove();
        map.current = null;
      }
    };
  }, []);

  return (
    <div className="rounded-2xl p-8 border flex flex-col gap-5 h-full" style={{ backgroundColor: '#0a2540', borderColor: 'rgba(255,255,255,0.05)' }}>
      <div className="w-12 h-12 rounded-xl bg-white flex items-center justify-center flex-shrink-0">
        <i className="bi bi-building text-xl" style={{ color: '#005eff' }} />
      </div>
      <h3 className="text-xl font-bold text-white" style={{ fontFamily: 'var(--font-primary)' }}>
        Unidades e Franquias
      </h3>
      <p className="text-white/50 text-sm leading-relaxed" style={{ fontFamily: 'var(--font-primary)' }}>
        Centros de distribuição em todos os estados brasileiros para entregas ágeis e pontuais.
      </p>

      <div
        ref={mapContainer}
        className="rounded-xl overflow-hidden border flex-1 min-h-[340px]"
        style={{ borderColor: 'rgba(255,255,255,0.1)' }}
      />

      <p className="text-white/30 text-xs" style={{ fontFamily: 'var(--font-primary)' }}>
        <i className="bi bi-circle-fill mr-1" style={{ color: '#005eff' }} />
        {CAPITAIS.length} centros de distribuição ativos
      </p>
    </div>
  );
}