-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 17, 2024 at 07:36 AM
-- Server version: 10.4.22-MariaDB
-- PHP Version: 8.0.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `proyek_mdp`
--
CREATE DATABASE IF NOT EXISTS `proyek_mdp` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `proyek_mdp`;

-- --------------------------------------------------------

--
-- Table structure for table `destinations`
--

CREATE TABLE `destinations` (
  `destination_id` int(11) NOT NULL,
  `destination_name` varchar(100) NOT NULL,
  `destination_latitude` float DEFAULT NULL,
  `destination_longtitude` float DEFAULT NULL,
  `destination_description` text DEFAULT NULL,
  `destination_poster` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `destinations`
--

INSERT INTO `destinations` (`destination_id`, `destination_name`, `destination_latitude`, `destination_longtitude`, `destination_description`, `destination_poster`) VALUES
(1, 'aling_aling_waterfall', NULL, NULL, 'Air terjun yang ada di Bali utara, tepatnya di Kabupaten Buleleng, ini juga punya kolam air yang dalam, mencapai 4 meter, dan sangat ideal untuk berenang. Air di sini juga bersih dan sangat segar', 1),
(2, 'bali_bird_park', NULL, NULL, 'salah satu taman burung yang terbesar di Indonesia. Kurang lebih terdapat 1.000 satwa dari 250 jenis spesies ungags yang berada di Bali Bird Park./n/nTak hanya dapat melihat berbagai macam burung-burung yang menarik secara langsung, namun pengunjung juga dapat merasakan sensasi berada di teater 4D.', 2),
(3, 'bali_gwk', NULL, NULL, 'terdapat patung tertinggi di Indonesia. Patung Garuda Wisnu Kencana memiliki tinggi 75 meter, dengan lebar patung 65 meter./n/nGWK Cultural Park is home to some of the most iconic cultural landmarks in Bali, including the magnificent Garuda Wisnu Kencana statue, which stands tall at 121 meters and is a representation of the Hindu god Vishnu and his mount, the Garuda bird./n/nfeatures a range of cultural activities and performances, including the Kecak Garuda Wisnu dance, traditional Balinese music and dance performances, and much more.', 7),
(4, 'bali_safari_marine_park', NULL, NULL, 'kebun binatang yang memiliki luas area sangat luas, sekitar 400,000 meter persegi./n/nsetiap jenis satwa berkeliaran bebas dalam sebuah area besar. Karena area sangat luas, agar pengunjung dapat melihat satwa secara langsung, pengunjung akan menaiki kendaraan safari. /n/nEnjoy Baliâ€™s best safari experience in the dark. This mesmerizing Night Safari Package is available every day! From witnessing zebras, elephants, and giraffes interacting with each other.', 2),
(5, 'bali_zoo', NULL, NULL, 'Take a walk on the wild side and getting up close and personal with the exotic animals from across the globe. Enjoy a wide range of fun and unique experiences during our signature animals encounters./n/nBaliâ€™s first zoological park is a wondrous place where you can learn the behaviour of over 500 rare and exotic animals in a lush, tropical environment. A park where you can participate in fascinating animal adventure activities, some of which are unique experiences of their kind in Indonesia./n/nBali Zoo merupakan taman satwa pertama di Bali di mana kamu bisa langsung melihat dan mempelajari lebih dari 500 satwa. ', 7),
(6, 'baloga', NULL, NULL, 'Taman hiburan fotogenik dengan pameran bunga, taman topiari, rumah kupu-kupu & wahana menunggang unta./n/nada banyak tanaman hias dan buah-buahan yang pas dijadikan spot foto kekinian. Selain refreshing, kalian bisa sekaligus belajar mengenai berbagai tanaman dan buah yang mungkin belum pernah ditemukan.', 8),
(7, 'bedugul', NULL, NULL, '', 9),
(8, 'bromo', NULL, NULL, 'Mount Bromo is an active somma volcano and part of the Tengger mountains/n/nkawah aktif yang mengepulkan asap putih dengan lautan pasirnya yang membentang luas di sekeliling kawah gunung bromo. ', 9),
(9, 'bukit_jaddih', NULL, NULL, 'lokasi bekas penambangan kapur yang menyisakan pemandangan indah.\n/n/nBerkeliling di antara tebing-tebing tinggi berwarna putih. Rasanya seperti sedang tersesat di labirin raksasa./n/nKawasan ini merupakan gundukan tambang batu kapur besar, luas 500 hektare', 8),
(10, 'campuhan_ridge_walk', NULL, NULL, 'Campuhan Ridge Walk sebenarnya merupakan sebuah trek untuk jogging yang lokasinya berada di sekitar perbukitan di Jalan Bangkiang Sidem', 2),
(11, 'gunung_batur_kintamani', NULL, NULL, 'Gunung Batur adalah merupakan sebuah gunung berapi aktif di Kecamatan Kintamani, Kabupaten Bangli, Bali, Indonesia./n/ngunung berapi purba di Bali dengan kalderanya yang sangat luas serta pemandangan danaunya yang menawan.\n/n/nGunung Batur merupakan salah satu gunung yang masih aktif dan memiliki kaldera di Bali.', 3),
(12, 'monkey_forest_ubud', NULL, NULL, 'anda bisa langsung berinteraksi dengan kera ekor panjang yang memang menghuni kawasan wisata ini. /n/nkera-kera memang hidup bebas. Warga setempat menganggap jika kera-kera tersebut adalah keramat yang tidak boleh diganggu. Sehingga anda benar-benar bisa merasakan suasana seperti menyatu dengan alam.', 6),
(13, 'new_kuta_golf', NULL, NULL, ' lokasi ini memang khusus dibuat sebagai tempat bermain golf yang cocok digunakan semua traveler./n/nlokasinya juga sangat indah dengan luas area lebih dari cukup untuk kawasan wisata golf./n/nSecara fisik, New Kuta Golf sangat menarik karena kontur tanahnya yang berbatasan langsung dengan laut dan tebing-tebingnya terjal.', 7),
(14, 'nusa_penida', NULL, NULL, 'This spectacular island is outlined by towering limestone cliffs and surrounded by azure waters. /n/nPerairan pulau Nusa Penida terkenal dengan kawasan selamnya di antaranya terdapat di Crystal Bay, Manta Point, Batu Meling, Batu Lumbung, Batu Abah, Toyapakeh dan Malibu Point.', 5),
(15, 'oneeighty', NULL, NULL, 'lifftop day club celebrating sophisticated beach culture 162 metres above the Indian Ocean. Built alongside a series of cascading ponds, spilling into a jaw-dropping glass-bottom sky pool extending 6 metres over the cliff edge.', 7),
(16, 'pantai_amed', NULL, NULL, 'Pantai dengan pasir biota lautnya yang beragam ini menawarkan keindahan alam yang luar biasa.\n/n/nPada tempat wisata ini juga terdapat danau yang bisa digunakan oleh pemula untuk belajar menyelam dengan melihat indahnya pemandangan laut.', 6),
(17, 'pantai_berawa', NULL, NULL, 'pantai di Kuta Utara yang mempunyai keindahan pasir dan ombak. Di sini Anda bisa berselancar dan berbagai aktivitas lainnya. Pemandangan di sekitar pantai juga sangat indah dan menarik./n/ndiminati oleh para peselancar, karena memiliki ombak yang cukup besar. Oleh sebab itu, pantai Berawa menjadi salah satu tempat yang sangat pas untuk para pecinta surfing.', 4),
(18, 'pantai_nunggalan', NULL, NULL, 'Nunggalan merupakan salah satu pantai pasir putih Bali yang sangat indah. Pantai ini cukup terpencil dan suasananya masih sangat sepi dan tenang./n/nAnda bisa bersantai menikmati angin sepoi-sepoi dan deburan ombak yang tenang di pantai ini.', 4),
(19, 'pantai_pandawa', NULL, NULL, 'pantai ini memiliki hamparan garis pantainya yang landai, bersih, serta bibir pantai yang agak luas, sehingga sangat cocok untuk dijadikan wisata bersama keluarga.\n\n/n/nAda banyak aktivitas yang bisa anda coba di tempat ini mulai dari berenang, bermain kano, mengunjungi 5 patung ksatria Pandawa, berselancar, merilekskan diri sejenak dengan pijat tradisional, dan masih banyak lainnya.', 4),
(20, 'pantai_sanur', NULL, NULL, 'Pantai ini merupakan salah satu pantai yang tenang di timur Kota Denpasar, Di sini Anda memang tidak bisa surfing tapi Anda bisa snorkeling dan diving./n/nKeindahan alam pantai Bali bisa Anda rasakan di Pantai Sanur ini and Aktivitas seperti berjemur dan berenang juga bisa Anda lakukan di pantai ini.', 2),
(21, 'pura_besakih', NULL, NULL, 'sebuah komplek Pura yang terletak di Desa Besakih, Kecamatan Rendang, Kabupaten Karangasem, Bali, Indonesia./n/nKomplek Pura Besakih terdiri dari 1 Pura Pusat (Pura Penataran Agung Besakih) dan 18 Pura Pendamping (1 Pura Basukian dan 17 Pura Lainnya).\n/n/nPengunjung yang liburan ke Pura Besakih akan melihat area pura yang sangat luas, arsitektur megah dan area yang asri. Dari area pura Besakih pengunjung juga dapat melihat pemandangan area sawah, dan bukit. ', 1),
(22, 'pura_gunung_kawi', NULL, NULL, 'Candi Gunung Kawi atau Candi Tebing Gunung Kawi adalah situs purbakala yang dilindungi di Bali', 1),
(23, 'pura_lempuyang_luhur', NULL, NULL, 'tempat suci umat Hindu ini cukup unik karena terdapat di perbukitan yang memiliki hutan yang hijau./n/nPengunjung juga dapat menikmati keindahan pemandangan Gunung Agung di samping menikmati keagungan bangunan pura.', 4),
(24, 'pura_tanah_lot', NULL, NULL, 'pura yang berada di atas batu karang besar dengan latar belakang laut selatan. Sementara itu, batu karang besar ini berada di tengah laut. Kemudian, pada saat air pasang, batu karang terlihat terpisah dari daratan./n/nkeindahan pemandangan matahari terbenam, dengan siluet pura Tanah Lot. Kemudian, berpadu dengan ombak laut selatan yang menerjang batu karang/n/ndua Pura yang terletak di atas batu besar. Satu terletak di atas bongkahan batu dan satunya terletak di atas tebing ', 4),
(25, 'pura_tirta_empul', NULL, NULL, 'Di dalam area pura Tirta Empul terdapat dua buah kolam besar dengan banyak pancoran air. Kedalaman air setinggi pinggang orang dewasa. Air di kolam sangat jernih dan sejuk. /n/nPada area kolam inilah umat Hindu melakukan ritual penyucian diri dengan cara membasahi badan dan kepala di bawah air pacoran .\n', 1),
(26, 'pura_uluwatu', NULL, NULL, '', 5),
(27, 'taman_kupu', NULL, NULL, '', 5),
(28, 'tegallalang_rice_terrace', NULL, NULL, 'Kaki bukit berundak indah yang memiliki sawah di tengah pepohonan rimbun, ditambah zip line dan ayunan hutan.', 8),
(29, 'tirta_gannga', NULL, NULL, 'Tempat wisata di Bali terkenal dengan kolam batu berisi ikan mas dan dikelilingi air mancur./n/ntaman air peninggalan Kerajaan Karangasem dan dibangun oleh Raja Karangasem pada tahun 1946. Nama taman tirta gangga memiliki sebuah arti air suci dari sungai gangga India. /n/nBangunan pertama atau bangunan tertinggi terdapat mata air yang berada di bawah pohon beringin. Bangungan kedua kamu dapat menemukan kolam renang, dan bangunan ketiga yang paling bawah ada air mancur serta kolam air. ', 4),
(30, 'tukad_cepung_waterfall', NULL, NULL, '', 9);

-- --------------------------------------------------------

--
-- Table structure for table `images`
--

CREATE TABLE `images` (
  `image_id` int(11) NOT NULL,
  `destination_id` int(11) NOT NULL,
  `image` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `images`
--

INSERT INTO `images` (`image_id`, `destination_id`, `image`) VALUES
(1, 1, './uploaded/aling_aling_waterfall/img1.jpg'),
(2, 1, './uploaded/aling_aling_waterfall/img2.jpg'),
(3, 2, './uploaded/bali_bird_park/img1.jpg'),
(4, 3, './uploaded/bali_gwk/img1.jpg'),
(5, 3, './uploaded/bali_gwk/img2.jpg'),
(6, 4, './uploaded/bali_safari_marine_park/img1.jpg'),
(7, 4, './uploaded/bali_safari_marine_park/img2.jpg'),
(8, 6, './uploaded/baloga/img1.jpg'),
(9, 6, './uploaded/baloga/img2.jpg'),
(10, 6, './uploaded/baloga/img3.jpg'),
(11, 7, './uploaded/bedugul/img1.jpg'),
(12, 7, './uploaded/bedugul/img2.jpg'),
(13, 8, './uploaded/bromo/img1.jpg'),
(14, 8, './uploaded/bromo/img2.jpg'),
(15, 9, './uploaded/bukit_jaddih/img1.jpg'),
(16, 10, './uploaded/campuhan_ridge_walk/img1.jpg'),
(17, 12, './uploaded/monkey_forest_ubud/img1.jpg'),
(18, 12, './uploaded/monkey_forest_ubud/img2.jpg'),
(19, 15, './uploaded/oneeighty/img2.jpg'),
(20, 16, './uploaded/pantai_amed/img1.jpg'),
(21, 16, './uploaded/pantai_amed/img2.png'),
(22, 17, './uploaded/pantai_berawa/img1.jpg'),
(23, 17, './uploaded/pantai_berawa/img2.jpg'),
(24, 18, './uploaded/pantai_nunggalan/img1.jpg'),
(25, 18, './uploaded/pantai_nunggalan/img2.jpg'),
(26, 19, './uploaded/pantai_pandawa/img1.jpg'),
(27, 19, './uploaded/pantai_pandawa/img2.jpg'),
(28, 20, './uploaded/pantai_sanur/img1.jpg'),
(29, 20, './uploaded/pantai_sanur/img2.jpg'),
(30, 22, './uploaded/pura_gunung_kawi/img1.jpg'),
(31, 22, './uploaded/pura_gunung_kawi/img2.jpg'),
(32, 22, './uploaded/pura_gunung_kawi/img3.jpg'),
(33, 23, './uploaded/pura_lempuyang_luhur/img1.jpg'),
(34, 23, './uploaded/pura_lempuyang_luhur/img2.jpg'),
(35, 24, './uploaded/pura_tanah_lot/img1.jpg'),
(36, 24, './uploaded/pura_tanah_lot/img2.jpg'),
(37, 25, './uploaded/pura_tirta_empul/img1.jpg'),
(38, 25, './uploaded/pura_tirta_empul/img2.jpg'),
(39, 26, './uploaded/pura_uluwatu/img1.jpg'),
(40, 26, './uploaded/pura_uluwatu/img2.jpg'),
(41, 26, './uploaded/pura_uluwatu/img3.jpg'),
(42, 27, './uploaded/taman_kupu/img1.png'),
(43, 27, './uploaded/taman_kupu/img2.jpg'),
(44, 27, './uploaded/taman_kupu/img3.jpg'),
(45, 28, './uploaded/tegallalang_rice_terrace/img1.jpg'),
(46, 29, './uploaded/tirta_gannga/img1.jpg'),
(47, 29, './uploaded/tirta_gannga/img2.jpg'),
(48, 30, './uploaded/tukad_cepung_waterfall/img1.jpg'),
(49, 30, './uploaded/tukad_cepung_waterfall/img2.jpg'),
(50, 30, './uploaded/tukad_cepung_waterfall/img3.jpg'),
(51, 30, './uploaded/tukad_cepung_waterfall/img4.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `reviews`
--

CREATE TABLE `reviews` (
  `review_id` int(11) NOT NULL,
  `reviewer` int(11) NOT NULL,
  `destination_id` int(11) NOT NULL,
  `review` text NOT NULL,
  `star` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `display_name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `email`, `password`, `display_name`) VALUES
(1, 'dummy1@example.com', 'asd', 'DUMMY_1'),
(2, 'dummy2@example.com', 'asd', 'DUMMY_2'),
(3, 'dummy3@example.com', 'asd', 'DUMMY_3'),
(4, 'dummy4@example.com', 'asd', 'DUMMY_4'),
(5, 'dummy5@example.com', 'asd', 'DUMMY_5'),
(6, 'dummy6@example.com', 'asd', 'DUMMY_6'),
(7, 'dummy7@example.com', 'asd', 'DUMMY_7'),
(8, 'dummy8@example.com', 'asd', 'DUMMY_8'),
(9, 'dummy9@example.com', 'asd', 'DUMMY_9'),
(59, 'dummy_acc@example.com', 'asd', 'dummy_account'),
(60, 'dummy_acc2@example.com', 'asd', 'dummy_account2'),
(68, 'asd@asd.com', 'asd', 'asd');

-- --------------------------------------------------------

--
-- Table structure for table `user_destinations`
--

CREATE TABLE `user_destinations` (
  `user_destination_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `destination_id` int(11) NOT NULL,
  `departure_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `destinations`
--
ALTER TABLE `destinations`
  ADD PRIMARY KEY (`destination_id`);

--
-- Indexes for table `images`
--
ALTER TABLE `images`
  ADD PRIMARY KEY (`image_id`),
  ADD KEY `destination_id` (`destination_id`);

--
-- Indexes for table `reviews`
--
ALTER TABLE `reviews`
  ADD PRIMARY KEY (`review_id`),
  ADD KEY `reviewer` (`reviewer`),
  ADD KEY `destination_id` (`destination_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `user_destinations`
--
ALTER TABLE `user_destinations`
  ADD PRIMARY KEY (`user_destination_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `destination_id` (`destination_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `destinations`
--
ALTER TABLE `destinations`
  MODIFY `destination_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT for table `images`
--
ALTER TABLE `images`
  MODIFY `image_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=52;

--
-- AUTO_INCREMENT for table `reviews`
--
ALTER TABLE `reviews`
  MODIFY `review_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=69;

--
-- AUTO_INCREMENT for table `user_destinations`
--
ALTER TABLE `user_destinations`
  MODIFY `user_destination_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `images`
--
ALTER TABLE `images`
  ADD CONSTRAINT `images_ibfk_1` FOREIGN KEY (`destination_id`) REFERENCES `destinations` (`destination_id`);

--
-- Constraints for table `reviews`
--
ALTER TABLE `reviews`
  ADD CONSTRAINT `reviews_ibfk_1` FOREIGN KEY (`reviewer`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `reviews_ibfk_2` FOREIGN KEY (`destination_id`) REFERENCES `destinations` (`destination_id`);

--
-- Constraints for table `user_destinations`
--
ALTER TABLE `user_destinations`
  ADD CONSTRAINT `user_destinations_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `user_destinations_ibfk_2` FOREIGN KEY (`destination_id`) REFERENCES `destinations` (`destination_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
