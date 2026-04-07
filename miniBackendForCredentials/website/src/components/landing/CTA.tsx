import { Button } from "@/components/ui/button";
import { ArrowRight, Heart } from "lucide-react";

const CTA = () => {
  return (
    <section className="py-24 gradient-soft">
      <div className="container mx-auto px-4">
        <div className="relative max-w-4xl mx-auto">
          {/* Background decoration */}
          <div className="absolute inset-0 gradient-hero rounded-3xl opacity-10 blur-2xl" />
          
          <div className="relative bg-card rounded-3xl p-8 md:p-12 shadow-glow text-center">
            <div className="inline-flex items-center justify-center w-16 h-16 rounded-full gradient-hero mb-6">
              <Heart className="w-8 h-8 text-primary-foreground" />
            </div>
            
            <h2 className="text-3xl md:text-4xl font-bold mb-4">
              Ready to Find Your Match?
            </h2>
            
            <p className="text-muted-foreground text-lg max-w-xl mx-auto mb-8">
              Join our growing community and start your journey to meaningful connections today.
            </p>
            
            <div className="flex flex-col sm:flex-row gap-4 justify-center">
              <Button variant="hero" size="xl">
                Start Matching
                <ArrowRight className="w-5 h-5" />
              </Button>
              <Button variant="hero-outline" size="xl">
                View API Docs
              </Button>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
};

export default CTA;
