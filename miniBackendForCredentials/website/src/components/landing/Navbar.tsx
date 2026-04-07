import { Button } from "@/components/ui/button";
import { Heart } from "lucide-react";

const Navbar = () => {
  return (
    <nav className="fixed top-0 left-0 right-0 z-50 bg-background/80 backdrop-blur-md border-b border-border/50">
      <div className="container justify-center">
        <div className="flex items-center justify-between h-16">
          {/* Logo */}
          <div className="flex items-center gap-3">
            <div className="w-8 h-8 rounded-lg bg-white flex">
              {/* <Heart className="w-4 h-4 text-primary-foreground" /> */}
              <img src="/datingappicon.png" alt="appIcon" />
            </div>
            <span className="text-xl font-bold">Dating App</span>
          </div>

          {/* Nav links */}
          {/* <div className="hidden md:flex items-center gap-8 text-sm">
            <a href="#features" className="text-muted-foreground hover:text-foreground transition-colors">Features</a>
            <a href="#docs" className="text-muted-foreground hover:text-foreground transition-colors">Documentation</a>
            <a href="#pricing" className="text-muted-foreground hover:text-foreground transition-colors">Pricing</a>
          </div> */}

        </div>
      </div>
    </nav>
  );
};

export default Navbar;
