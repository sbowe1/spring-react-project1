import type { Metadata } from "next";
import { Inter } from "next/font/google";
import MenuBar from "./ui/MenuBar";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "Learning Platform",
  description: "Create your own customized learning plans.",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={inter.className}>
        <MenuBar />
        {children}
      </body>
    </html>
  );
}
