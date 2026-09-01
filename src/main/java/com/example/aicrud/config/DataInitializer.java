package com.example.aicrud.config;

import com.example.aicrud.entity.Product;
import com.example.aicrud.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DataInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            log.info("Seeding initial 100 product catalog items (20 products per category across 5 categories)...");

            List<Product> products = new ArrayList<>();

            // =========================================================================
            // 1. Electronics (20 products)
            // =========================================================================
            products.add(createProduct("Wireless Noise-Canceling Headphones",
                    "Premium over-ear wireless headphones with active noise cancellation, 40-hour battery life, and high-fidelity sound.",
                    "Electronics", 199.99, 45, "Top-tier audio gear with ANC and memory foam earcups.", "audio, headphones, wireless, noise-canceling"));
            products.add(createProduct("Mechanical Gaming Keyboard RGB",
                    "Customizable mechanical keyboard with tactile brown switches, per-key RGB backlighting, and durable PBT keycaps.",
                    "Electronics", 119.00, 8, "Pro-grade mechanical keyboard for responsive typing.", "gaming, keyboard, rgb, mechanical"));
            products.add(createProduct("4K Ultra-HD Monitor 27-inch",
                    "IPS display with 3840x2160 resolution, HDR400 support, 99% sRGB color gamut, and 65W USB-C charging.",
                    "Electronics", 349.99, 12, "Crystal clear 4K IPS display for creative professionals.", "monitor, 4k, display, ips, usb-c"));
            products.add(createProduct("Ergonomic Wireless Mouse",
                    "Vertical ergonomic wireless mouse with adjustable DPI and silent click buttons to prevent wrist strain.",
                    "Electronics", 59.99, 35, "Comfortable ergonomic design reducing muscle tension.", "mouse, ergonomic, wireless, peripheral"));
            products.add(createProduct("USB-C 10-in-1 Docking Station",
                    "Multi-port hub with dual 4K HDMI, Gigabit Ethernet, 100W Power Delivery, and SD card reader.",
                    "Electronics", 79.50, 22, "Complete desktop connectivity in a compact aluminum body.", "docking-station, usb-c, hub, multi-port"));
            products.add(createProduct("Portable Bluetooth Speaker Waterproof",
                    "IPX7 waterproof outdoor Bluetooth speaker delivering 360-degree deep bass with 24-hour playtime.",
                    "Electronics", 49.99, 60, "Rugged waterproof portable sound for outdoor adventures.", "speaker, bluetooth, waterproof, audio"));
            products.add(createProduct("Smart Home Wi-Fi Security Camera",
                    "1080p full HD indoor smart camera with night vision, two-way audio, and AI motion detection alerts.",
                    "Electronics", 69.00, 18, "Reliable 24/7 home security with intelligent phone alerts.", "smart-home, camera, security, wifi"));
            products.add(createProduct("High-Speed NVMe 2TB PCIe SSD",
                    "Ultra-fast PCIe Gen4 NVMe internal solid state drive with up to 7000MB/s read speeds for gaming and editing.",
                    "Electronics", 159.99, 4, "Blazing fast storage for demanding workloads and gaming.", "ssd, storage, nvme, pcie, fast"));
            products.add(createProduct("Magnetic Fast Wireless Charger Pad",
                    "15W fast magnetic charging stand compatible with smart phones, smartwatches, and wireless earbuds.",
                    "Electronics", 29.99, 75, "Sleek 3-in-1 inductive charging stand.", "wireless-charger, fast-charging, magnetic, accessories"));
            products.add(createProduct("Studio Condenser USB Microphone",
                    "Cardioid condenser microphone with boom arm, shock mount, and zero-latency headphone monitoring.",
                    "Electronics", 89.95, 14, "Broadcast-quality crystal voice capture for podcasts and streams.", "microphone, studio, podcast, streaming, usb"));
            products.add(createProduct("1080p 60FPS Streaming Webcam",
                    "Full HD webcam with autofocus, dual stereo microphones, and physical privacy shutter.",
                    "Electronics", 64.99, 28, "Crisp 60fps video quality for remote work and meetings.", "webcam, streaming, video, hd, 60fps"));
            products.add(createProduct("Dual-Band Wi-Fi 6 Mesh Router",
                    "Next-generation Wi-Fi 6 mesh system covering up to 4,000 sq ft with speeds up to 3Gbps.",
                    "Electronics", 129.99, 9, "Whole-home high-speed wireless coverage without dead zones.", "router, wifi-6, mesh, networking"));
            products.add(createProduct("Smartwatch with Heart Rate & GPS",
                    "Fitness smartwatch featuring AMOLED touchscreen, continuous SpO2 monitor, GPS, and 7-day battery.",
                    "Electronics", 179.00, 30, "Comprehensive health and workout tracking on your wrist.", "smartwatch, fitness, gps, health, wearable"));
            products.add(createProduct("Portable Power Bank 20000mAh",
                    "Heavy-duty fast charge battery pack with 65W Power Delivery output capable of charging laptops.",
                    "Electronics", 39.99, 50, "High-capacity power bank for mobile and laptop charging on the go.", "powerbank, battery, 20000mah, portable"));
            products.add(createProduct("Smart LED Light Strip 5M",
                    "App-controlled RGBIC LED strip with music sync, timer schedules, and voice assistant integration.",
                    "Electronics", 24.99, 80, "Vibrant room lighting with millions of customizable colors.", "smart-lighting, led, rgb, smart-home"));
            products.add(createProduct("Active Stylus Pen for Touchscreens",
                    "Precision digital pen with 4096 levels of pressure sensitivity, palm rejection, and tilt support.",
                    "Electronics", 34.50, 40, "Natural handwriting and sketching tool for digital tablets.", "stylus, digital-pen, drawing, tablet"));
            products.add(createProduct("True Wireless Earbuds with ANC",
                    "Compact in-ear wireless earphones with active noise cancellation and IPX5 sweat resistance.",
                    "Electronics", 89.99, 6, "Immersive audio with customizable touch controls.", "earbuds, wireless, anc, audio"));
            products.add(createProduct("Digital Drawing Tablet 10-inch",
                    "Graphic drawing pad with battery-free stylus and 8 customizable shortcut keys for digital art.",
                    "Electronics", 109.99, 16, "Intuitive drawing surface for illustrators and designers.", "drawing-tablet, graphic, stylus, creative"));
            products.add(createProduct("Electric Screwdriver Kit Precision",
                    "Rechargeable cordless electric screwdriver with 24 magnetic precision bits and LED shadowless light.",
                    "Electronics", 42.00, 25, "Versatile tool kit for repairing electronics, laptops, and phones.", "tools, screwdriver, precision, repair"));
            products.add(createProduct("Compact Laser Color Printer",
                    "Wireless duplex color laser printer delivering fast 22ppm output with mobile cloud printing.",
                    "Electronics", 229.00, 7, "High-efficiency laser printing for home office documents.", "printer, laser, wireless, office"));

            // =========================================================================
            // 2. Furniture (20 products)
            // =========================================================================
            products.add(createProduct("Ergonomic Mesh Office Chair",
                    "Breathable mesh chair featuring adjustable lumbar support, 3D armrests, and dynamic tilt.",
                    "Furniture", 289.50, 18, "Ergonomically engineered for long work sessions with superior lumbar comfort.", "furniture, office, ergonomic, chair"));
            products.add(createProduct("Electric Height-Adjustable Standing Desk",
                    "Motorized sit-stand desk with dual motors, 4 memory presets, and cable management tray (55x28 inch).",
                    "Furniture", 429.00, 10, "Smooth height transitions promoting healthy posture throughout the workday.", "desk, standing-desk, motorized, ergonomic"));
            products.add(createProduct("Solid Wood Coffee Table with Storage",
                    "Rustic solid oak coffee table with open lower shelf and double sliding storage drawers.",
                    "Furniture", 189.00, 15, "Timeless natural wood aesthetic with ample living room storage.", "table, coffee-table, solid-wood, living-room"));
            products.add(createProduct("Minimalist Bookshelf 5-Tier",
                    "Industrial open bookcase with sturdy steel frame and vintage rustic wood shelves.",
                    "Furniture", 119.99, 22, "Durable 5-shelf display unit for books, plants, and collectibles.", "bookshelf, storage, industrial, shelving"));
            products.add(createProduct("Mid-Century Modern Armchair",
                    "Tufted upholstered accent reading chair with tapered solid walnut wooden legs.",
                    "Furniture", 249.99, 5, "Elegant accent chair offering plush comfort and vintage charm.", "armchair, accent-chair, mid-century, living-room"));
            products.add(createProduct("Dual Monitor Arm Desk Mount",
                    "Heavy-duty gas spring dual monitor mount supporting screens up to 32 inches with full 360-degree rotation.",
                    "Furniture", 59.95, 34, "Frees up valuable desk space and optimizes ergonomic viewing angles.", "monitor-arm, desk-mount, ergonomic, workspace"));
            products.add(createProduct("Ergonomic Memory Foam Footrest",
                    "Under-desk foot rest cushion with dual height adjustments and washable non-slip cover.",
                    "Furniture", 32.50, 55, "Relieves leg and back fatigue during prolonged desk sitting.", "footrest, ergonomic, office, comfort"));
            products.add(createProduct("Scandinavian Dining Table Set",
                    "Solid beech dining table with 4 cushioned ergonomic dining chairs in Nordic white style.",
                    "Furniture", 499.00, 8, "Clean minimalist Scandinavian dining set for modern homes.", "dining-table, dining-set, scandinavian, chairs"));
            products.add(createProduct("Leather Executive Swivel Chair",
                    "High-back bonded leather executive office chair with padded armrests and tilt-lock mechanism.",
                    "Furniture", 349.99, 11, "Premium executive seating combining elegance and plush support.", "chair, executive, leather, office"));
            products.add(createProduct("Compact Wall-Mounted Floating Desk",
                    "Space-saving fold-down floating wall desk with hidden internal organizational shelves.",
                    "Furniture", 89.00, 19, "Smart folding workstation ideal for small apartments and dorms.", "desk, floating-desk, wall-mount, compact"));
            products.add(createProduct("Bedside Nightstand with Wireless Charging",
                    "Modern 2-drawer end table with integrated Qi wireless charging surface and USB ports.",
                    "Furniture", 79.99, 25, "Smart bedside table keeping devices powered seamlessly.", "nightstand, end-table, bedroom, wireless-charging"));
            products.add(createProduct("Velvet Accent Vanity Chair",
                    "Soft velvet petal-back vanity chair with gold-plated metal legs and high-density foam seat.",
                    "Furniture", 139.00, 14, "Glamorous velvet vanity chair adding luxury to bedrooms.", "chair, velvet, vanity, bedroom"));
            products.add(createProduct("Industrial Entryway Coat Rack & Bench",
                    "Hall tree organizer with shoe storage bench, 9 dual hooks, and top display shelf.",
                    "Furniture", 99.50, 20, "All-in-one entryway coat and shoe organizer.", "entryway, coat-rack, bench, organizer"));
            products.add(createProduct("Modular 3-Piece Sectional Sofa",
                    "L-shaped comfortable fabric sectional sofa with reversible chaise lounge and deep cushions.",
                    "Furniture", 899.00, 3, "Spacious modular seating built for family comfort.", "sofa, sectional, couch, living-room"));
            products.add(createProduct("Adjustable Floor Reading Lamp",
                    "Modern minimalist floor standing lamp with warm LED bulb and flexible rotating arm.",
                    "Furniture", 54.99, 42, "Targeted glare-free illumination for reading and relaxing.", "lamp, floor-lamp, lighting, living-room"));
            products.add(createProduct("Bamboo Shoe Rack Bench 3-Tier",
                    "Natural bamboo entryway shoe storage bench holding up to 8 pairs with padded seat top.",
                    "Furniture", 44.90, 38, "Eco-friendly entryway bench making shoe changing convenient.", "shoe-rack, bamboo, bench, entryway"));
            products.add(createProduct("Ergonomic Kneeling Posture Chair",
                    "Angled ergonomic kneeling chair with thick memory foam cushions to encourage upright spinal alignment.",
                    "Furniture", 129.00, 9, "Active sitting chair reducing lower back pressure.", "chair, kneeling-chair, ergonomic, posture"));
            products.add(createProduct("Geometric Wall Shelf Set of 3",
                    "Floating hexagonal wall decorative shelves made of solid paulownia wood and matte iron wire.",
                    "Furniture", 39.99, 65, "Stylish wall art shelving for plants and photo frames.", "wall-shelf, floating-shelf, decor, wood"));
            products.add(createProduct("Reclining Gaming Chair with Footrest",
                    "High-back racing gaming chair with retractable footrest, lumbar support, and 150-degree recline.",
                    "Furniture", 199.99, 13, "Immersive ergonomic gaming throne for marathon sessions.", "gaming-chair, recliner, footrest, gaming"));
            products.add(createProduct("Foldable Solid Wood Luggage Rack",
                    "Hotel-style folding suitcase stand with heavy-duty nylon straps and lower shoe shelf.",
                    "Furniture", 36.00, 27, "Convenient guest room luggage stand for effortless packing.", "luggage-rack, bedroom, folding, wood"));

            // =========================================================================
            // 3. Kitchen (20 products)
            // =========================================================================
            products.add(createProduct("Organic Bamboo Cutting Board Set",
                    "Set of 3 natural bamboo cutting boards with deep juice grooves, side handles, and antibacterial organic finish.",
                    "Kitchen", 34.95, 60, "Eco-friendly, knife-friendly organic bamboo cutting boards.", "kitchen, cutting-board, bamboo, eco-friendly"));
            products.add(createProduct("Stainless Steel French Press Coffee Maker",
                    "Double-wall insulated 34 oz coffee press that keeps beverages hot for hours with 4-level filtration.",
                    "Kitchen", 29.99, 5, "Durable stainless steel French press delivering rich sediment-free coffee.", "coffee, french-press, stainless-steel, kitchen"));
            products.add(createProduct("Digital Air Fryer 6-Quart",
                    "Touchscreen air fryer with 8 one-touch cooking presets, rapid air circulation, and non-stick basket.",
                    "Kitchen", 99.99, 25, "Crispy fried favorites with up to 85% less oil.", "air-fryer, cooking, kitchen, healthy"));
            products.add(createProduct("Professional Chef Knife 8-Inch Japanese Steel",
                    "High-carbon German steel kitchen knife with razor-sharp 15-degree edge and pakkawood handle.",
                    "Kitchen", 69.95, 30, "Precision balanced chef knife for slicing, dicing, and chopping.", "chef-knife, cutlery, japanese-steel, kitchen"));
            products.add(createProduct("Non-Stick Ceramic Cookware Set 10-Piece",
                    "Toxin-free non-stick pots and pans set with scratch-resistant ceramic coating and stay-cool handles.",
                    "Kitchen", 179.99, 12, "Healthy non-stick cooking set free of PTFE, PFOA, and lead.", "cookware, pots, pans, ceramic, non-stick"));
            products.add(createProduct("Countertop Espresso Machine 15-Bar",
                    "Semi-automatic Italian pump espresso maker with stainless steel steam milk frother wand.",
                    "Kitchen", 149.00, 7, "Barista-quality rich crema espresso and lattes at home.", "espresso, coffee-machine, barista, kitchen"));
            products.add(createProduct("Electric Gooseneck Pour-Over Kettle",
                    "Variable temperature electric kettle with precise 1-degree control and 60-minute keep-warm mode.",
                    "Kitchen", 49.99, 40, "Precision pouring gooseneck spout for artisanal drip coffee.", "kettle, gooseneck, pour-over, coffee, electric"));
            products.add(createProduct("High-Speed Blender 1200W",
                    "Professional countertop blender with 6 stainless steel blades for smoothies, crushed ice, and soups.",
                    "Kitchen", 89.99, 18, "Powerful motor pulverizing tough ingredients in seconds.", "blender, smoothie, high-speed, kitchen"));
            products.add(createProduct("Cast Iron Dutch Oven 6-Quart Enameled",
                    "Heavy-duty enameled cast iron Dutch oven with lid for braising, baking artisan bread, and stews.",
                    "Kitchen", 79.95, 15, "Superior heat retention and even heat distribution.", "dutch-oven, cast-iron, baking, cookware"));
            products.add(createProduct("Airtight Food Storage Containers 12-Piece",
                    "BPA-free pantry organization canisters with 4-hinge locking lids and chalkboard labels.",
                    "Kitchen", 39.99, 50, "Keeps pantry staples like flour, pasta, and cereal fresh and organized.", "storage, containers, airtight, pantry"));
            products.add(createProduct("Digital Kitchen Food Scale",
                    "High-precision kitchen scale with LCD display, tare function, and 0.1g measurement accuracy.",
                    "Kitchen", 18.99, 85, "Essential precision scale for baking, cooking, and portion control.", "scale, kitchen-scale, baking, precision"));
            products.add(createProduct("Silicone Heat-Resistant Utensil Set",
                    "12-piece cooking utensils set with natural acacia wood handles and food-grade silicone heads.",
                    "Kitchen", 24.99, 70, "Non-scratch heat-resistant cooking tools safe for all pans.", "utensils, silicone, cooking-tools, kitchen"));
            products.add(createProduct("Automatic Burr Coffee Grinder",
                    "Flat burr grinder with 18 precise grind settings from fine espresso to coarse French press.",
                    "Kitchen", 59.99, 6, "Uniform coffee grinds maximizing aroma and flavor extraction.", "coffee-grinder, burr, fresh-coffee, kitchen"));
            products.add(createProduct("Sous Vide Precision Cooker 1000W",
                    "Immersion circulator with digital touch panel, quiet operation, and accurate water temperature control.",
                    "Kitchen", 84.50, 14, "Restaurant-quality tender meats cooked to exact doneness.", "sous-vide, precision-cooking, culinary, gourmet"));
            products.add(createProduct("Bread Maker Machine with 15 Programs",
                    "Automatic bread maker with gluten-free setting, delay timer, and viewing window (2 lb capacity).",
                    "Kitchen", 119.00, 8, "Bake fresh warm homemade bread with zero kneading hassle.", "bread-maker, baking, appliance, kitchen"));
            products.add(createProduct("Rotating Spice Rack with 20 Jars",
                    "Stainless steel revolving countertop carousel with 20 pre-filled glass spice jars and sift caps.",
                    "Kitchen", 36.99, 32, "Organized 360-degree spice access saving counter space.", "spice-rack, organization, jars, seasoning"));
            products.add(createProduct("Stainless Steel Mixing Bowls Set of 5",
                    "Nesting metal mixing bowls with airtight silicone lids, non-slip bottoms, and inner measurement marks.",
                    "Kitchen", 28.50, 45, "Versatile bowls for meal prep, salad tossing, and food storage.", "mixing-bowls, stainless-steel, baking, meal-prep"));
            products.add(createProduct("Smart Meat Thermometer Wireless",
                    "Bluetooth long-range wireless probe monitoring internal meat temperature with smartphone alerts.",
                    "Kitchen", 64.99, 22, "Never overcook steaks, roasts, or barbecue again.", "thermometer, meat-thermometer, bbq, wireless"));
            products.add(createProduct("Japanese Mandoline Food Slicer",
                    "Adjustable vegetable slicer with 3 interchangeable stainless steel blades and safety hand guard.",
                    "Kitchen", 27.99, 38, "Paper-thin uniform slices for salads, gratins, and garnishes.", "mandoline, slicer, kitchen-gadget, vegetables"));
            products.add(createProduct("Induction Cooktop Burner Portable",
                    "Single burner induction hot plate with 1800W power, digital timer, and safety child lock.",
                    "Kitchen", 54.00, 16, "Fast energy-efficient induction cooking anywhere.", "induction, cooktop, portable, burner"));

            // =========================================================================
            // 4. Books (20 products)
            // =========================================================================
            products.add(createProduct("Designing Data-Intensive Applications",
                    "Comprehensive guide to distributed systems, data storage, reliability, and scalable architecture.",
                    "Books", 44.99, 30, "The definitive reference for backend and distributed systems engineering.", "books, programming, distributed-systems, backend"));
            products.add(createProduct("Clean Code: Handbook of Software Craftsmanship",
                    "Robert C. Martin's timeless guide on writing clean, readable, and maintainable object-oriented code.",
                    "Books", 39.95, 45, "Foundational principles for writing elegant, refactorable software.", "books, clean-code, best-practices, java"));
            products.add(createProduct("The Pragmatic Programmer: 20th Anniversary Edition",
                    "Classic career-defining insights on software architecture, tooling, continuous learning, and pragmatism.",
                    "Books", 42.50, 28, "Essential wisdom for becoming an effective professional developer.", "books, programming, career, software-engineering"));
            products.add(createProduct("System Design Interview – An Insider's Guide",
                    "Alex Xu's step-by-step visual framework for acing distributed system design interviews.",
                    "Books", 36.00, 50, "Visual architectural breakdowns of real-world scalable platforms.", "books, system-design, interview, architecture"));
            products.add(createProduct("Head First Design Patterns 2nd Edition",
                    "Brain-friendly visual guide explaining GoF design patterns with practical, real-world examples.",
                    "Books", 48.99, 20, "Master object-oriented design patterns with engaging visual explanations.", "books, design-patterns, oop, java"));
            products.add(createProduct("Spring Boot in Action",
                    "Hands-on guide to building production-ready microservices and REST APIs with Spring Boot.",
                    "Books", 37.99, 18, "Master auto-configuration, actuators, security, and cloud deployment.", "books, spring-boot, java, microservices"));
            products.add(createProduct("Microservices Patterns: With Examples in Java",
                    "Chris Richardson's comprehensive patterns for decomposing monolithic apps into scalable services.",
                    "Books", 49.99, 15, "Definitive patterns covering Saga, CQRS, and event sourcing.", "books, microservices, saga, architecture"));
            products.add(createProduct("Effective Java 3rd Edition",
                    "Joshua Bloch's definitive guide of 90 best practices for modern Java development.",
                    "Books", 41.50, 6, "Must-read rules for writing robust and idiomatic Java.", "books, java, effective-java, best-practices"));
            products.add(createProduct("Refactoring: Improving Design of Existing Code",
                    "Martin Fowler's classic catalog of refactoring techniques with clear before-and-after walkthroughs.",
                    "Books", 44.00, 25, "Systematic techniques for safely restructuring messy legacy codebases.", "books, refactoring, clean-code, agile"));
            products.add(createProduct("Building Microservices: Designing Fine-Grained Systems",
                    "Sam Newman's updated guide on modeling, deploying, and securing enterprise microservice ecosystems.",
                    "Books", 46.99, 19, "Holistic architectural guidance for modern cloud services.", "books, microservices, cloud, architecture"));
            products.add(createProduct("Continuous Delivery: Reliable Software Releases",
                    "Jez Humble & David Farley's foundational book on automated deployment pipelines and CI/CD.",
                    "Books", 45.00, 14, "Core practices for automating reliable, zero-downtime releases.", "books, devops, ci-cd, automation"));
            products.add(createProduct("Domain-Driven Design: Tackling Complexity",
                    "Eric Evans' seminal work on building rich domain models aligned with real business processes.",
                    "Books", 52.00, 8, "Deep strategic patterns for modeling complex enterprise software.", "books, ddd, architecture, domain-driven-design"));
            products.add(createProduct("Site Reliability Engineering (Google SRE)",
                    "How Google builds, operates, and monitors reliable distributed cloud systems at scale.",
                    "Books", 38.50, 32, "Google's operational blueprint for high-availability systems.", "books, sre, devops, google, reliability"));
            products.add(createProduct("Modern Java in Action: Lambdas, Streams, Functional",
                    "Complete guide to modern Java language features: lambdas, streams, reactive, and concurrency.",
                    "Books", 43.99, 22, "Master modern functional programming techniques in Java.", "books, java, functional, streams"));
            products.add(createProduct("Clean Architecture: A Craftsman's Guide",
                    "Robert C. Martin's blueprint for creating decoupled, testable, and maintainable software architectures.",
                    "Books", 34.99, 40, "Core universal architectural rules for lasting software systems.", "books, architecture, clean-architecture, software"));
            products.add(createProduct("Cloud Native Patterns: Designing Change-tolerant Software",
                    "Practical guide on building resilient cloud-native applications with auto-scaling and self-healing.",
                    "Books", 47.50, 11, "Patterns for containerized, resilient cloud services.", "books, cloud-native, kubernetes, patterns"));
            products.add(createProduct("Database Internals: A Deep Dive into Distributed Systems",
                    "In-depth guide exploring storage engines, B-trees, LSM trees, consensus, and distributed databases.",
                    "Books", 46.00, 9, "Under-the-hood exploration of storage engines and consensus algorithms.", "books, database, storage-engine, distributed-systems"));
            products.add(createProduct("Grokking Algorithms: An Illustrated Guide",
                    "Friendly visual introduction to data structures, sorting, binary search, graphs, and dynamic programming.",
                    "Books", 29.99, 55, "Visual beginner-friendly guide to computer science algorithms.", "books, algorithms, data-structures, computer-science"));
            products.add(createProduct("Enterprise Integration Patterns",
                    "Gregor Hohpe's classic catalog of messaging patterns for integrating distributed enterprise applications.",
                    "Books", 54.99, 7, "Comprehensive messaging solutions for enterprise system integration.", "books, integration, messaging, patterns"));
            products.add(createProduct("Software Architecture: The Hard Parts",
                    "Modern analysis on distributed architectures, trade-offs, service granularity, and data contracts.",
                    "Books", 48.00, 16, "Pragmatic decision-making framework for architectural trade-offs.", "books, architecture, distributed, microservices"));

            // =========================================================================
            // 5. Fitness (20 products)
            // =========================================================================
            products.add(createProduct("Smart Water Bottle with Hydration Tracker",
                    "Vacuum-insulated 24 oz smart bottle with LED sensor puck tracking water intake via companion app.",
                    "Fitness", 49.90, 24, "Smart hydration tracking bottle keeping water cold for 24 hours.", "fitness, hydration, smart-bottle, wellness"));
            products.add(createProduct("Deep Tissue Percussion Massage Gun",
                    "Handheld quiet massage device with 6 speed levels and 5 interchangeable muscle recovery heads.",
                    "Fitness", 79.99, 15, "Relieves muscle soreness and accelerates post-workout recovery.", "massage-gun, recovery, fitness, therapy"));
            products.add(createProduct("Adjustable Dumbbells Set 5-52.5 lbs",
                    "Quick-turn selector dial dumbbells replacing 15 sets of weights in a compact home space.",
                    "Fitness", 299.00, 8, "Compact all-in-one dumbbell set for home strength training.", "dumbbells, weights, strength, home-gym"));
            products.add(createProduct("High-Density Extra Thick Yoga Mat",
                    "Eco-friendly non-slip TPE workout mat with alignment lines and carrying strap (6mm thick).",
                    "Fitness", 34.99, 65, "Superior joint cushioning and traction for yoga and Pilates.", "yoga-mat, fitness, pilates, eco-friendly"));
            products.add(createProduct("Fabric Resistance Bands Set of 5",
                    "Non-slip durable cloth loop bands with varied resistance levels for glute and leg workouts.",
                    "Fitness", 19.99, 80, "Premium fabric booty bands that don't roll or pinch skin.", "resistance-bands, workout, legs, fitness"));
            products.add(createProduct("Compact Foldable Treadmill with Incline",
                    "Under-desk motorized walking and running pad with LCD console and Bluetooth speaker.",
                    "Fitness", 399.00, 4, "Space-saving folding treadmill for home and office workouts.", "treadmill, cardio, running, home-gym"));
            products.add(createProduct("Speed Jump Rope with Ball Bearings",
                    "Adjustable tangle-free steel wire skipping rope with memory foam handles for cardio training.",
                    "Fitness", 14.99, 90, "Smooth 360-degree rotation for fast calorie burning.", "jump-rope, cardio, crossfit, conditioning"));
            products.add(createProduct("Foam Roller for Muscle Recovery",
                    "High-density grid trigger point foam roller for myofascial release and physical therapy.",
                    "Fitness", 22.50, 50, "Relieves tight knots and improves mobility and flexibility.", "foam-roller, recovery, mobility, stretching"));
            products.add(createProduct("Pull-Up Bar for Doorway",
                    "No-screw ergonomic multi-grip pull-up and chin-up bar supporting up to 400 lbs.",
                    "Fitness", 38.99, 28, "Upper body workout tool for doorway pull-ups, push-ups, and dips.", "pull-up-bar, calisthenics, upper-body, gym"));
            products.add(createProduct("Smart Body Fat Scale Bluetooth",
                    "Bio-impedance smart scale measuring 13 body metrics: weight, BMI, body fat %, and muscle mass.",
                    "Fitness", 29.99, 45, "Syncs body composition progress seamlessly with health apps.", "smart-scale, body-fat, health, fitness"));
            products.add(createProduct("Magnetic Resistance Exercise Bike",
                    "Stationary indoor cycling bike with quiet belt drive, tablet holder, and pulse sensors.",
                    "Fitness", 279.00, 6, "Smooth whisper-quiet cardio cycling for home workouts.", "exercise-bike, cycling, cardio, indoor-gym"));
            products.add(createProduct("Kettlebell Cast Iron 20 kg",
                    "Solid cast iron kettlebell with wide textured grip handle and color-coded weight ring.",
                    "Fitness", 49.99, 18, "Build full-body power, stamina, and core strength.", "kettlebell, weights, strength, crossfit"));
            products.add(createProduct("Core Ab Roller Wheel with Knee Pad",
                    "Dual-wheel ultra-wide abdominal exercise roller with non-slip rubber tread and padded handles.",
                    "Fitness", 18.50, 60, "Strengthens and tones core, abs, and shoulders effectively.", "ab-roller, core, abs, workout"));
            products.add(createProduct("Ankle & Wrist Weights Pair",
                    "Adjustable silicone strap weights (2 lbs each) for walking, aerobics, and resistance training.",
                    "Fitness", 21.99, 35, "Comfortable wearable weights adding burn to everyday walks.", "wrist-weights, ankle-weights, aerobics, fitness"));
            products.add(createProduct("Gym Duffle Bag with Shoe Compartment",
                    "Water-resistant sports gym bag with ventilated shoe pocket and wet towel compartment.",
                    "Fitness", 39.99, 40, "Functional multi-compartment travel bag for workouts and trips.", "duffle-bag, gym-bag, travel, accessories"));
            products.add(createProduct("Padded Weightlifting Wrist Wraps",
                    "Heavy-duty elastic wrist support straps with thumb loops for bench press and Olympic lifting.",
                    "Fitness", 16.99, 75, "Maximum wrist stability preventing injuries during heavy lifts.", "wrist-wraps, lifting, powerlifting, gym"));
            products.add(createProduct("Slam Ball Workout Medicine Ball 15 lbs",
                    "No-bounce heavy textured rubber slam ball for explosive core conditioning and CrossFit.",
                    "Fitness", 36.00, 20, "Durable slam ball engineered for high-velocity throws.", "slam-ball, medicine-ball, conditioning, power"));
            products.add(createProduct("Incline Decline Weight Bench Foldable",
                    "Adjustable 7-position commercial grade strength bench for dumbbell presses and core workouts.",
                    "Fitness", 129.99, 9, "Sturdy foldable bench supporting full-body strength routines.", "weight-bench, workout-bench, home-gym, strength"));
            products.add(createProduct("Balance Board Trainer with Roller",
                    "Hardwood balance board with non-slip grip surface for surf training, core stability, and agility.",
                    "Fitness", 54.99, 17, "Enhances balance, reaction time, and core stabilization.", "balance-board, coordination, core, balance"));
            products.add(createProduct("Suspension Trainer Home Gym Kit",
                    "All-in-one bodyweight resistance straps with door anchor and extension strap for full-body workouts.",
                    "Fitness", 69.95, 22, "Versatile portable suspension fitness system using bodyweight.", "suspension-trainer, bodyweight, fitness, portable"));

            productRepository.saveAll(products);
            log.info("Successfully seeded all {} sample products across {} categories (20 per category).",
                    products.size(), productRepository.findAllCategories().size());
        }
    }

    private Product createProduct(String name, String description, String category, double price, int stock, String aiSummary, String aiTags) {
        return Product.builder()
                .name(name)
                .description(description)
                .category(category)
                .price(BigDecimal.valueOf(price))
                .stockQuantity(stock)
                .aiSummary(aiSummary)
                .aiTags(aiTags)
                .build();
    }
}
