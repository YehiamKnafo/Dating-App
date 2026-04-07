import { Heart, MessageCircle, Shield, Sparkles, Users, Zap } from "lucide-react";

const features = [
  {
    icon: Sparkles,
    title: "Smart Matching",
    description: "Our algorithm learns your preferences to find compatible matches.",
  },
  {
    icon: Shield,
    title: "Verified Profiles",
    description: "Every profile is verified to ensure authentic connections.",
  },
  {
    icon: MessageCircle,
    title: "Seamless Chat",
    description: "Built-in messaging with read receipts and media sharing.",
  },
  {
    icon: Users,
    title: "Active Community",
    description: "Thousands of singles looking for meaningful relationships.",
  },
  {
    icon: Zap,
    title: "Fast & Reliable",
    description: "Lightning-fast API with 99.9% uptime guarantee.",
  },
  {
    icon: Heart,
    title: "Success Stories",
    description: "Hundreds of couples have found love through our platform.",
  },
];

const Features = () => {
  return (
    <section className="py-24 bg-background">
      <div className="container mx-auto px-4">
        {/* Section header */}
        <div className="text-center max-w-2xl mx-auto mb-16">
          <h2 className="text-3xl md:text-4xl font-bold mb-4">
            Everything You Need to{" "}
            <span className="text-gradient">Connect</span>
          </h2>
          <p className="text-muted-foreground text-lg">
            Powerful features designed to help you find and nurture real relationships.
          </p>
        </div>

        {/* Features grid */}
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
          {features.map((feature, index) => (
            <div
              key={index}
              className="group p-6 rounded-2xl bg-card gradient-card shadow-soft hover:shadow-glow transition-all duration-300 hover:-translate-y-1"
            >
              <div className="w-12 h-12 rounded-xl gradient-hero flex items-center justify-center mb-4 group-hover:scale-110 transition-transform duration-300">
                <feature.icon className="w-6 h-6 text-primary-foreground" />
              </div>
              <h3 className="text-xl font-semibold mb-2">{feature.title}</h3>
              <p className="text-muted-foreground">{feature.description}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
};

export default Features;
