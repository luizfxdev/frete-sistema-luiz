"use client";

import Image from "next/image";
import { useEffect, useState } from "react";

const LOGOS = Array.from({ length: 10 }, (_, i) => `/assets/images/icon${i + 1}.png`);

export function ParceiroCarrossel() {
  const [mounted, setMounted] = useState(false);

  useEffect(() => {
    setMounted(true);
  }, []);

  return (
    <section className="py-16 bg-white border-y border-gray-100">
      <div className="max-w-7xl mx-auto px-6 mb-8">
        <p
          className="text-center text-xs font-bold uppercase tracking-widest text-gray-400"
          style={{ fontFamily: "var(--font-primary), -apple-system, sans-serif" }}
        >
          Empresas que confiam na NextLog
        </p>
      </div>
      <div className="relative overflow-hidden">
        <div className="absolute left-0 top-0 bottom-0 w-24 bg-gradient-to-r from-white to-transparent z-10 pointer-events-none" />
        <div className="absolute right-0 top-0 bottom-0 w-24 bg-gradient-to-l from-white to-transparent z-10 pointer-events-none" />
        {mounted && (
          <div
            className="flex w-max"
            style={{
              animation: "scroll-x 30s linear infinite",
            }}
          >
            {[...LOGOS, ...LOGOS, ...LOGOS].map((src, i) => (
              <div
                key={i}
                className="flex-shrink-0 w-40 h-20 mx-8 flex items-center justify-center grayscale hover:grayscale-0 opacity-50 hover:opacity-100 transition-all duration-300"
              >
                <Image
                  src={src}
                  alt={`Parceiro ${(i % 10) + 1}`}
                  width={140}
                  height={56}
                  className="object-contain w-full h-full p-2"
                />
              </div>
            ))}
          </div>
        )}
      </div>
      <style jsx>{`
        @keyframes scroll-x {
          0% {
            transform: translateX(0);
          }
          100% {
            transform: translateX(calc(-160px * 10 - 64px * 10));
          }
        }
      `}</style>
    </section>
  );
}