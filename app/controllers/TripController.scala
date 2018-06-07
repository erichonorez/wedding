package controllers

import javax.inject._

import play.api.mvc._
import services.TripRepository

class TripController @Inject()(tripRepository: TripRepository, cc: ControllerComponents)(implicit assetsFinder: AssetsFinder)
 extends AbstractController(cc) {

    def index = Action {
      val trip = tripRepository.get.get
        /*val trip = Trip(List(
            Step(
                "Christchurch",
                Location(
                    -43.525650,
                    172.639847
                ),
                renderer.render(markdown),
                List(
                    "christchurch_2.jpg",
                    "christchurch_4.jpg",
                    "christchurch_5.jpg",
                    "christchurch_6.jpg",
                    "christchurch_7.jpg"
                )
            ),
            Step(
                "Lake Tekapo",
                Location(
                    -43.8833298,
                    170.5166646
                ),
                "Le village de Tekapo est une station balnéaire pittoresque au bord du lac au coeur du pays de Mackenzie dans l'île du sud de la Nouvelle-Zélande. Il tire son nom du superbe lac Tekapo, l'un des lacs du sud des Alpes du Sud. Entouré par certains des plus hauts sommets de Nouvelle-Zélande, y compris Mt Cook, et loin de la lumière des lumières de la ville, le célèbre ciel sans nuages du lac Tekapo en fait l'un des meilleurs endroits dans l'hémisphère sud pour voir le ciel nocturne. Le statut de patrimoine mondial est actuellement recherché pour l'ensemble de la zone en tant que première zone de préservation des étoiles. Le petit village en plein essor de Tekabo est idéalement situé à l'extrémité sud du lac, et est centré autour d'une belle rangée de cafés tentants et de boutiques de cadeaux pittoresques. Avec ses magnifiques vues dégagées sur les eaux turquoises et les collines enneigées, il n'est pas étonnant que cette petite ville devienne rapidement l'une des destinations naturelles préférées de la Nouvelle-Zélande.",
                List(
                    "lake_tekapo_1.jpg",
                    "lake_tekapo_2.jpg",
                    "lake_tekapo_3.jpg",
                    "lake_tekapo_4.jpg"
                )
            ),
            Step(
                "Mount Cook National Park",
                Location(
                    -43.7333304,
                    170.08999964
                ),
                "Aussi connu sous le nom de parc national d'Aoraki, cette réserve de 700 kilomètres carrés abrite 19 montagnes qui dépassent les 3000m, y compris le plus haut sommet de l'Australasie - le Mt. 3754m. Aoraki. Le paysage incroyable fait partie du patrimoine mondial du sud-ouest de la Nouvelle-Zélande et peut être exploré par l'escalade, la randonnée, le ski et même des excursions en vol panoramique. Ne pas manquer le glacier Tasman; À 27 km de long et 3 km de large, c'est le plus grand glacier du pays - même si ces dernières années, il a malheureusement fondu rapidement.",
                List(
                    "mount_cook_2.jpg",
                    "mount_cook_3.jpg",
                    "mount_cook_4.jpg",
                    "mount_cook_5.jpg",
                    "mount_cook_6.jpg",
                    "mount_cook_7.jpg"
                )
            )
        ))*/
                /* {
                            name: "Mount Cook National Park",
                            location: {
                                lat: -43.7333304,
                                lng: 170.08999964
                            },
                            description: "Aussi connu sous le nom de parc national d'Aoraki, cette réserve de 700 kilomètres carrés abrite 19 montagnes qui dépassent les 3000m, y compris le plus haut sommet de l'Australasie - le Mt. 3754m. Aoraki. Le paysage incroyable fait partie du patrimoine mondial du sud-ouest de la Nouvelle-Zélande et peut être exploré par l'escalade, la randonnée, le ski et même des excursions en vol panoramique. Ne pas manquer le glacier Tasman; À 27 km de long et 3 km de large, c'est le plus grand glacier du pays - même si ces dernières années, il a malheureusement fondu rapidement.",
                            pictures: [
                                "mount_cook_2.jpg",
                                "mount_cook_3.jpg",
                                "mount_cook_4.jpg",
                                "mount_cook_5.jpg",
                                "mount_cook_6.jpg",
                                "mount_cook_7.jpg"
                            ]
                        }, {
                            name: "Dunedin",
                            location: {
                                lat: -45.87416,
                                lng: 170.50361
                            },
                            description: "Dunedin et sa région, le Coastal Otago, offrent une beauté naturelle doublée d'une histoire culturelle fascinante, qui vaut souvent à la ville le titre d'éco-capitale de la Nouvelle-Zélande. La côte d'Otago s'étend du Waitaki au nord d'Oamaru jusqu'à l'impressionnant fleuve Clutha au sud de Dunedin. Parmi les incontournables, on peut citer les mystérieuses roches de Moeraki. Commencez au nord. Le district de Waitaki en Nouvelle-Zélande est un lieu d'une beauté naturelle envoûtante, avec de vertes pâtures et des villages de pêcheurs de carte postale. Arrêtez-vous à Oamaru et admirez son architecture historique en pierre blanche, un paysage urbain époustouflant qui domine une communauté modeste. Tunnel beach Après avoir fait une promenade d'environ 20 minutes, vous pourrez rejoindre l'un des sites les plus étonnants qui se trouve sur la péninsule d'Otago ... La plage, minuscule, est accessible par un tunnel, mais l'arc que vous pouvez voir au-dessus de vous est assez impressionnant Baldwin Street La Baldwin Street de Dunedin est réputée pour être la rue la plus pentue au monde. Située au nord-est de la ville, à proximité des jardins botaniques, elle vaut le détour pour faire ",
                            pictures: [
                                "dunedin_1.jpg",
                                "dunedin_2.jpg",
                                "dunedin_3.jpg",
                                "dunedin_5.jpg",
                                "dunedin_6.jpg"
                            ]
                        }, {
                            name: "Lake Te Anau",
                            location: {
                                lat: -45.1999992,
                                lng: 167.7999968
                            },
                            description: "Le lac Te Anau est situé dans le sud-ouest de l'île du sud de la Nouvelle-Zélande et est le deuxième plus grand lac du comté, couvrant une superficie de 344 kilomètres carrés. Autrefois résidence des tribus maories, les rives du lac rencontrent les montagnes couvertes de forêts du parc national du Fiordland à l'ouest et les collines ondoyantes à l'est. Les activités dans la région comprennent la pêche, la chasse au cerf, les sports nautiques et la possibilité d'explorer deux des sentiers de randonnée les plus connus du pays; la piste Milford et la piste Kepler.",
                            pictures: [
                                "lake_te_anau_1.jpg",
                                "lake_te_anau_2.jpg",
                                "lake_te_anau_3.jpg",
                                "lake_te_anau_4.jpg"
                            ]
                        }, {
                            name: "Queenstown",
                            location: {
                                lat: -45.031162,
                                lng: 168.662643
                            },
                            description: "Queenstown est situé sur les rives du lac Wakatipu et offre une vue imprenable sur les sommets alpins environnants. Considéré par beaucoup comme l'une des capitales mondiales de l'aventure, il offre aux visiteurs un large choix d'activités de stimulation de l'adrénaline, comme le saut à l'élastique, le rafting, la tyrolienne, le ski et le parachutisme. Queenstown possède également un bar animé et un restaurant, et pour ceux qui préfèrent les choses plus calmes dans la vie, il y a des vignobles, des terrains de golf, des spas et des centres de bien-être.",
                            pictures: [
                                "queenstown_1.jpg",
                                "queenstown_3.jpg",
                                "queenstown_4.jpg",
                                "queenstown_5.jpg",
                                "queenstown_6.jpg",
                                "queenstown_7.jpg"
                            ]
                        }, {
                            name: "Franz Josef Glacier",
                            location: {
                                lat: -43.4668631325,
                                lng: 170.188249247
                            },
                            description: "La ville pittoresque de la côte ouest de Franz Josef a quelques centaines de résidents permanents. C'est le pays des glaciers, et le glacier Franz Josef (d'où le nom de la ville) et Fox Glacier, à environ 23 km plus au sud, sont les principales attractions. La ville offre une myriade d'options pour un hébergement confortable, surtout après les activités de neige et de glace - combiner un tour en hélicoptère avec une promenade guidée sur le terrain glaciaire pour voir les grottes de glace, et des crevisses et des pinacles spectaculaires. Des excursions en hélicoptère au-dessus des deux glaciers sont également disponibles, avec une escale à la tête de l'un ou l'autre glacier afin que vous puissiez découvrir le paysage gelé de près.",
                            pictures: [
                                "franz_josef_1.jpg",
                                "franz_josef_2.jpg",
                                "franz_josef_3.jpg",
                                "franz_josef_5.jpg",
                            ]
                        }, {
                            name: "Punakaiki",
                            location: {
                                lat: -42.10841,
                                lng: 171.33619
                            },
                            description: "Le parc national de Paparoa s'étend des sommets de la chaîne de montagnes Paparoa à la côte ouest de l'île du Sud, englobant des paysages spectaculaires sur tout le long du chemin. Les plus remarquables sont les falaises de calcaire, les canyons, les trous de soufflage et les formations de «pancake-stack» datant de 30 millions d'années. Le petit village de Punakaiki se trouve dans le parc et offre aux visiteurs la possibilité de visiter des sites du patrimoine, d'admirer l'art et l'artisanat et de passer du temps dans les cafés et les restaurants locaux.",
                            pictures: [
                                "punakaiki_1.jpg",
                                "punakaiki_2.jpg",
                                "punakaiki_3.jpg",
                                "punakaiki_4.jpg"
                            ]
                        }, {
                            name: "Marahau",
                            location: {
                                lat: -41.0,
                                lng: 173.0
                            },
                            description: "Le parc national Abel Tasman est situé au nord de l’île. Les plages y sont superbes (celle de Totaranui notamment), parfois adossées à des collines boisées, parfois à des forets primitives. La Coast Track, le sentier du littoral, est très fréquenté l’été. Le kayak reste cependant la meilleure façon d’appréhender les côtes de l’Abel Tasman National Park, pour y voir les otaries, les manchots et les dauphins.",
                            pictures: [
                                "marahau_1.jpg",
                                "marahau_2.jpg",
                                "marahau_3.jpg",
                                "marahau_4.jpg"
                            ]
                        }, {
                            name: "Wellington",
                            location: {
                                lat: -41.28664,
                                lng: 174.77557
                            },
                            description: "Située sur la pointe sud-ouest de l'île du Nord, dans le détroit de Cook, Wellington est la capitale constitutionnelle et culturelle de la Nouvelle-Zélande, surnommée «la petite capitale la plus cool du monde». Avec son architecture diversifiée, ses musées de classe mondiale, ses attractions culturelles et ses restaurants primés, Wellington est une destination populaire pour les voyageurs locaux et internationaux. En raison de son emplacement dans les «Roaring Forties» la ville connaît sa juste part de vent et par conséquent, la voile est une activité populaire ici - avec des charters offrant aux visiteurs l'expérience d'une croisière détendue avec de belles vues sur la ville et ses environs baies. Liens utiles: https://www.wellingtonnz.com/",
                            pictures: [
                                "wellington_1.jpg",
                                "wellington_2.jpg",
                                "wellington_3.jpg",
                                "wellington_4.jpg",
                                "wellington_6.jpg",
                                "wellington_7.jpg",
                                "wellington_8.jpg"
                            ]
                        }, {
                            name: "Tongariro National Park",
                            location: {
                                lat: -39.272709,
                                lng: 175.5802278
                            },
                            description: "Tongariro est le plus ancien parc national de Nouvelle-Zélande, situé au cœur de l'île du Nord. Il comprend trois volcans actifs - le mont Ngauruhoe, le mont Tongariro et le mont Ruapehu (l'un des volcans les plus actifs au monde) - qui ont une grande importance pour le peuple maori local. La beauté brute de l'environnement attire un grand nombre de touristes, et c'est aussi la raison pour laquelle une grande partie de la trilogie du film Le Seigneur des Anneaux a été filmée ici. Il y a des activités à apprécier par tout le monde ici, des croisières scéniques au rafting, à la randonnée, à l'escalade et au ski en hiver.",
                            pictures: [
                                "tongariro_1.jpg",
                                "tongariro_2.jpg",
                                "tongariro_3.jpg",
                                "tongariro_5.jpg"
                            ]
                        }, {
                            name: "Rotorua",
                            location: {
                                lat: -38.4666648,
                                lng: 176.6999972
                            },
                            description: "Rotorua est le lieu où l'on observe le plus clairement les forces turbulentes qui ont créé la Nouvelle-Zélande. Cette ville, située sur le plateau volcanique, est l'un des sites géothermiques les plus actifs au monde, et est bâtie directement sur la Ceinture de feu du Pacifique. Rotorua est aussi la terre ancestrale des Te Arawa , un peuple arrivé ici il y a plus de 600 ans, et dont la présence offre de nombreuses expériences culturelles aux visiteurs. Essayez un festin de hangi (cuit dans le sol fumant), suivez une visite d'un authentique village maori pré-européen, ou offrez-vous un agréable soin thermal. Si vous aimez l'aventure, Rotorua possède de nombreuses attractions pour faire monter l'adrénaline : tout ce que vous pouvez imaginer, du saut en parachute à la luge, en passant par le zorbing (qui consiste à dévaler une pente dans une bulle en plastique), et l'un des meilleurs circuits de VTT de Nouvelle-Zélande. C'est aussi une grande région de pêche à la truite, dans les lacs et les rivières affluentes, et si vous n'avez pas de chance là-bas, vous pouvez toujours aller admirer (mais pas les attraper, hélas) les énormes truites des sources Rainbow et Fairy. Avec son aéroport international, Rotorua est aussi la porte vers les stations de l'Île du Nord pour profiter au mieux du ski et du snowboard sur le Mt Ruapehu en hiver.",
                            pictures: [
                                "rotorua_1.jpg",
                                "rotorua_2.jpg",
                                "rotorua_3.jpg",
                                "rotorua_4.jpg",
                                "rotorua_6.jpg",
                                "rotorua_7.jpg"
                            ]
                        }, {
                            name: "Pauanui",
                            location: {
                                lat: 20.701283,
                                lng: -156.173325
                            },
                            description: "Il y a beaucoup d'endroits magnifiques dans le monde, mais certains sont vraiment spéciaux ! Avec ses milliers de coins de nature, ses jolies plages, ses forêts préservées, ses habitants sympas et sa table de produits frais, la péninsule du Coromandel est une destination unique à ne pas manquer ! Une destination favorite des kiwis, le Coromandel est seulement à une heure et demie de route de l'aéroport d'Auckland et aussi de Rotorua, et pourtant c'est une région à l'écart des sentiers battus. Volez, roulez ou naviguez jusqu'au Coromandel et là, en pleine nature, rencontrez des gens qui vous diront pourquoi le coin est si bon à vivre. Deux heures avant et après la marée basse, il suffit de creuser dans le sable de la plage de Hot Water Beach pour avoir un bain (très) chaud. Pour ceux qui préfèrent la version luxe, visitez The Lost Spring à Whitianga. Ainsi nommée à cause de l'arche rocheuse qui permet le passage entre deux plages isolées, Cathedral Cove est à voir absolument lors d'une visite de la péninsule. L'arche donne une dimension architecturale au site, tandis que les plages sont bordées de Pohutukawas, les “arbres de Noël” de la NZ, qui donnent une ombre idéale pour le pique-nique. L'incroyable vallée de Kauaeranga, en amont de Thames, est le point de départ des sentiers qui mènent aux Pinnacles, un des refuges les plus populaires de Nouvelle-Zélande. Tout au nord du Coromandel, le sentier côtier “Coromandel costal walkway” vous offrira des vues exceptionnelles sur les îles Cuvier et Great Barrier, et sur le golfe d'Hauraki. Une des meilleures randonnée à la journée en NZ. La ruée vers l'or du Coromandel s'est produite entre 1869 et 1871 et le minerai se ramassait littéralement “à la pelle”. De nombreux vestiges de cette histoire minière du Coromandel sont visibles aujourd'hui et vous pourrez profiter de visites guidées, ou bien les explorer par vous même à Thames, à Waihi, dans la gorge de Karangahake, ou bien le long des 82 km de piste cyclable du Hauraki Rail Trail. S'amuser n'est pas réservé aux enfants, tout le monde aimera le petit train de Driving Creek. Il serpente à travers la végétation indigène et vous conduira à travers une réserve naturelle et un atelier de poterie. Ne manquez pas de faire un saut à Waterworks sur la route 309 toute proche.",
                            pictures: [
                                "pauanui_1.jpg",
                                "pauanui_3.jpg",
                                "pauanui_4.jpg",
                                "pauanui_5.jpg",
                            ]
                        }, {
                            name: "Paihia",
                            location: {
                                lat: -35.2833322,
                                lng: 174.083333
                            },
                            description: "S'étendant d'Auckland à l'extrémité septentrionale de la Nouvelle-Zélande, la région du Northland abrite des paysages variés comprenant de superbes plages isolées, des terres agricoles vallonnées et des paysages accidentés, offrant aux touristes de nombreuses activités telles que la plongée, le VTT, la randonnée ou simplement le tourisme. La région est mieux connue sur la 800 km Twin Coast Discovery Highway qui mène aux anciennes forêts de Kauri sur la côte ouest, la baie des îles et la ville de Whangerei à l'est, et au cap Reinga, où la mer de Tasman et l'océan Pacifique entrent en collision. .",
                            pictures: [
                                "northland_1.jpg",
                                "northland_2.jpg",
                                "northland_3.jpg",
                                "northland_4.jpg",
                                "northland_5.jpg",
                                "northland_7.jpg",
                            ]
                        }, {
                            name: "Auckland",
                            location: {
                                lat: -36.848461,
                                lng: 174.763336
                            },
                            description: "La ville d’Auckland est située sur un isthme, dans la partie nord de l’île. Une grande partie de la population Néo-Zélandaise vit dans cette immense agglomération. Auckland est très belle, entourée de volcans éteints, et de plages sublimes, Piha en tête. Queen Street, l’artère principale attire pour ses boutiques, tandis que la marina, repérable grâce à la Sky Tower, concentre beaucoup des monuments historiques de la ville. Imaginez un environnement urbain où chacun vit à moins d'une demi-heure de belles plages, de sentiers de randonnées et d'une douzaine d'îles de vacances enchanteresses. Ajoutez-y le climat ensoleillé, le contexte culturel polynésien et une passion pour les produits et vins d'exception et vous commencerez à comprendre à quoi ressemble Auckland, notre ville la plus grande et la plus diversifiée. Située sur l'étendue de terre la plus étroite de l'île du Nord, la ville s'étire littéralement d'un coin à l'autre du pays, de l'Océan Pacifique à la Mer de Tasmanie. Ainsi, où que vous vous trouviez à Auckland, vous ne serez jamais loin de la mer. Et quelle mer magnifique ! Des plages sauvages idéales pour les surfeurs au golfe de Hauraki, la mer et toutes ses attractions illustrent pourquoi on surnomme Auckland la Ville des voiles. L'île Waiheke se trouve à 40 minutes en ferry du centre-ville d'Auckland, et c'est la plus peuplée de toutes les îles du Golfe de Hauraki. Le paysage est un mélange pittoresque de terres agricoles, de forêts, de plages, de vignobles et d'oliveraies. En maori, Auckland porte le nom de Tamaki-Makau-Rau - signifiant « la vierge aux cent prétendants » car c'était une région convoitée par de nombreuses tribus. D'ailleurs, c'est toujours le cas aujourd'hui, le mode de vie d'Auckland étant classé parmi les plus enviables à travers le monde.",
                            pictures: [
                                "auckland_1.jpg",
                                "auckland_2.jpg",
                                "auckland_4.jpg",
                                "auckland_5.jpg",
                                "auckland_6.jpg",
                                "auckland_7.jpg",
                                "auckland_9.jpg",
                                "auckland_10.jpg",
                                "auckland_11.jpg",
                                "auckland_12.jpg",
                            ]
                        }

                        ]
                    }
                },*/

        Ok(views.html.trip.index(trip))
    }

    def step(slug: String) = Action { request =>
      val trip = tripRepository.get.get
      val step = trip.steps.find(s => s.slug.equals(slug)).get
      Ok(views.html.trip.step(step, request.headers.get("referrer").getOrElse(routes.TripController.index().url)))
    }
}