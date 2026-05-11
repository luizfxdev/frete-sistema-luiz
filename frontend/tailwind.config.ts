import type { Config } from 'tailwindcss'

const config: Config = {
  content: [
    './src/**/*.{js,ts,jsx,tsx,mdx}',
  ],
  theme: {
    extend: {
      fontFamily: {
        sans: ['var(--font-primary)'],
        serif: ['var(--font-secondary)'],
      },
      colors: {
        brand: {
          DEFAULT: '#0a2540',
          50: '#e8eef5',
          100: '#c5d3e3',
          200: '#9eb6cf',
          300: '#7798bb',
          400: '#5a82ac',
          500: '#3d6c9d',
          600: '#326095',
          700: '#27518a',
          800: '#1c4280',
          900: '#0a2540',
        },
        accent: {
          DEFAULT: '#00b37e',
          50: '#e0f7ef',
          100: '#b3ecd6',
          200: '#80dfba',
          300: '#4dd29e',
          400: '#26c889',
          500: '#00be75',
          600: '#00b37e',
          700: '#00a061',
          800: '#008e54',
          900: '#006c3c',
        },
        ink: '#0b1220',
        cream: '#f8f6f1',
      },
      animation: {
        'marquee': 'marquee 35s linear infinite',
        'fade-up': 'fade-up 0.8s ease forwards',
        'bounce-slow': 'bounce-slow 2s ease-in-out infinite',
        'shimmer': 'shimmer 3s ease-in-out infinite',
        'glow': 'glow 2s ease-in-out infinite alternate',
      },
      keyframes: {
        marquee: {
          '0%': { transform: 'translateX(0)' },
          '100%': { transform: 'translateX(-50%)' },
        },
        'fade-up': {
          '0%': { opacity: '0', transform: 'translateY(30px)' },
          '100%': { opacity: '1', transform: 'translateY(0)' },
        },
        'bounce-slow': {
          '0%, 100%': { transform: 'translateY(0)' },
          '50%': { transform: 'translateY(12px)' },
        },
        shimmer: {
          '0%, 100%': { opacity: '0.5' },
          '50%': { opacity: '1' },
        },
        glow: {
          '0%': { boxShadow: '0 0 20px rgba(0, 179, 126, 0.3)' },
          '100%': { boxShadow: '0 0 40px rgba(0, 179, 126, 0.6)' },
        },
      },
      boxShadow: {
        'soft': '0 8px 30px rgba(0, 0, 0, 0.08)',
        'elevated': '0 20px 60px rgba(10, 37, 64, 0.15)',
        'brand': '0 10px 40px rgba(10, 37, 64, 0.25)',
      },
    },
  },
  plugins: [],
}
export default config