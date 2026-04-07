
const Footer = () => {
  return (
    <footer className="py-12 bg-background border-t border-border ">
      <div className="container mx-auto px-4">
        <div className="flex flex-col md:flex-row items-center justify-between gap-6">
          {/* Logo */}
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 rounded-lg bg-white flex items-center justify-center">
              {/* <Heart className="w-4 h-4 text-primary-foreground" /> */}
              <img src="/datingappicon.png" alt="appIcon" />
            </div>
            <span className="text-xl font-bold">Dating App</span>
          </div>

          {/* Links */}
          {/* <nav className="flex flex-wrap items-center justify-center gap-6 text-sm text-muted-foreground">
            <a href="#" className="hover:text-foreground transition-colors">Documentation</a>
            <a href="#" className="hover:text-foreground transition-colors">API Reference</a>
            <a href="#" className="hover:text-foreground transition-colors">Pricing</a>
            <a href="#" className="hover:text-foreground transition-colors">Support</a>
          </nav> */}

          {/* Copyright */}
          <p className="text-sm text-muted-foreground">
            © 2026 Yehiam Knafo. All rights reserved.
          </p>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
