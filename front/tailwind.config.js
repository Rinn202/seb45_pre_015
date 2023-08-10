/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{html,js,jsx,ts,tsx}"],
  theme: {
    extend: {
      keyframes: {
        toast: {
          from: { transform: "translate(-50%, 100%)" },
          to: { transform: "translate(-50%, 0)" },
        },
      },
    },
    colors: {
      transparent: "transparent",
      current: "currentColor",
      black: "#000000",
      white: "#ffffff",
      primary: {
        100: "#fce3cf",
        150: "#fbd3b3",
        200: "#f9be8e",
        300: "#f8ae71",
        350: "#f69B51",
        400: "#f48225",
        500: "#f2740d",
        600: "#da680b",
        700: "#bd5a0a",
        800: "#a54f09",
        900: "#8d4307",
      },
      secondary: {
        50: "#E1ECF4",
        100: "#C8E0F4",
        150: "#A5CFED",
        200: "#7AB7E4",
        300: "#58A4DE",
        350: "#348FD5",
        400: "#0074CC",
        500: "#0065B2",
        600: "#005799",
        700: "#00457A",
        800: "#013761",
        900: "#002847",
      },
      danger: {
        100: "#FBD0D5",
        200: "#F9B1B9",
        300: "#F47382",
        400: "#EF233C",
        500: "#DC1128",
        600: "#B70E22",
        700: "#920B1B",
        800: "#6E0814",
        900: "#49060D",
      },
      soGray: {
        headerbg: "#F8F9F9",
        bg: "#F1F2F3",
        light: "#C8CCD0",
        normal: "#9099A0",
        icon: "#838C95",
        darker: "#51595F",
        footerbg: "#242629",
      },
      buttonPrimary: "#0A95FF",
      buttonSecondary: "#E1ECF4",
      select: "#2E7044",
      selected: "#5eba7d",
    },
    fontSize: {
      xxs: "0.7rem",
      xxl: "1.9rem",
      xs: ["0.75rem", { lineHeight: "1rem" }],
      sm: ["0.875rem", { lineHeight: "1.25rem" }],
      base: ["1rem", { lineHeight: "1.5rem" }],
      lg: ["1.125rem", { lineHeight: "1.75rem" }],
      xl: ["1.25rem", { lineHeight: "1.75rem" }],
      "2xl": ["1.5rem", { lineHeight: "2rem" }],
      "3xl": ["1.875rem", { lineHeight: "2.25rem" }],
      "4xl": ["2.25rem", { lineHeight: "2.5rem" }],
      "5xl": ["3rem", { lineHeight: "1" }],
      "6xl": ["3.75rem", { lineHeight: "1" }],
      "7xl": ["4.5rem", { lineHeight: "1" }],
      "8xl": ["6rem", { lineHeight: "1" }],
      "9xl": ["8rem", { lineHeight: "1" }],
    },
  },
  plugins: [require("@tailwindcss/line-clamp")],
};
