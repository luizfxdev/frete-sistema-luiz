"use client";

import { useEffect } from "react";
import AOS from "aos";
import "aos/dist/aos.css";
import { Navbar } from "@/features/landing/components/Navbar";
import { HeroSection } from "@/features/landing/components/HeroSection";
import { SobreSection } from "@/features/landing/components/SobreSection";
import { ServicosSection } from "@/features/landing/components/ServicosSection";
import { ParceiroCarrossel } from "@/features/landing/components/ParceiroCarrossel";
import { RastrearForm } from "@/features/landing/components/RastrearForm";
import { Footer } from "@/features/landing/components/Footer";

export default function LandingPage() {
  useEffect(() => {
    AOS.init({ duration: 700, once: true, easing: "ease-out-cubic" });
  }, []);

  return (
    <>
      <Navbar />
      <HeroSection />
      <SobreSection />
      <ServicosSection />
      <ParceiroCarrossel />
      <RastrearForm />
      <Footer />
    </>
  );
}