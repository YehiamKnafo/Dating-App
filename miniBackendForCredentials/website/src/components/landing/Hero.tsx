import { Button } from "@/components/ui/button";
import { Download } from "lucide-react";

const Hero = () => {
  const HandleDownload = ()=>{
    window.location.href = "https://github.com/YehiamKnafo/Dating-App/releases/latest/download/DatingApp-Installer.msi"
  }

  return (
    <section className=" h-90 flex items-center justify-center overflow-hidden gradient-soft">
      {/* Decorative elements */}


      <div className="container ">
        <div className="container flex justify-center">
          <img src="datingapplogo.png" alt="" />
  
        </div>
        
        
        <div className="mx-auto text-center">
          {/* Badge */}
   

          {/* Heading */}
          {/* <h1 className="text-5xl md:text-6xl lg:text-7xl font-bold  mb-6">
            Find Your{" "}
            <span className="text-gradient">Perfect Match</span>
          </h1> */}

          

          {/* CTA Buttons */}
          <h1>version 1.0.0 - beta</h1>
          <div className="flex justify-center mb-60">
            
            <Button onClick={HandleDownload} variant="hero" size="xl">
              <Download className="w-5 h-5" />
              {/* download icon  */}
              Download
            </Button>
         
          </div>

          {/* Social proof */}
  
        </div>
      </div>
    </section>
  );
};

export default Hero;
