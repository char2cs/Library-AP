-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: db
-- Generation Time: Oct 20, 2024 at 01:51 PM
-- Server version: 5.7.44
-- PHP Version: 8.2.24

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `libreria`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`%` PROCEDURE `CreateAdministrativo` (IN `p_dni` INT, IN `p_nombre` VARCHAR(255), IN `p_apellido` VARCHAR(255), IN `p_telefono` INT, IN `p_legajo` INT, OUT `p_perfil_id` INT)   BEGIN
    INSERT INTO Perfil (dni, nombre, apellido, telefono)
    VALUES (p_dni, p_nombre, p_apellido, p_telefono);

    SET p_perfil_id = LAST_INSERT_ID();

    INSERT INTO Administrativo (legajo, perfil_id)
    VALUES (p_legajo, p_perfil_id);
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `CreateCliente` (IN `p_dni` INT, IN `p_nombre` VARCHAR(255), IN `p_apellido` VARCHAR(255), IN `p_telefono` INT, IN `p_email` VARCHAR(255), OUT `p_perfil_id` INT)   BEGIN
    INSERT INTO Perfil (dni, nombre, apellido, telefono)
    VALUES (p_dni, p_nombre, p_apellido, p_telefono);

    SET p_perfil_id = LAST_INSERT_ID();

    INSERT INTO Cliente (email, perfil_id)
    VALUES (p_email, p_perfil_id);
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `DeleteAdministrativo` (IN `id` INT(11))   BEGIN

DELETE FROM Administrativo WHERE Administrativo.perfil_id = id;
DELETE FROM Perfil WHERE Perfil.id = id;

END$$

CREATE DEFINER=`root`@`%` PROCEDURE `DeleteCliente` (IN `id` INT(11))   BEGIN

DELETE FROM Cliente WHERE Cliente.perfil_id = id;
DELETE FROM Perfil WHERE Perfil.id = id;

END$$

CREATE DEFINER=`root`@`%` PROCEDURE `UpdateAdministrativo` (IN `p_id` INT, IN `p_dni` INT, IN `p_nombre` VARCHAR(255), IN `p_apellido` VARCHAR(255), IN `p_telefono` INT, IN `p_legajo` INT)   BEGIN
    UPDATE Perfil
    SET dni = p_dni, nombre = p_nombre, apellido = p_apellido, telefono = p_telefono
    WHERE id = p_id;

    UPDATE Administrativo
    SET legajo = p_legajo
    WHERE Administrativo.perfil_id = p_id;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `UpdateCliente` (IN `p_id` INT, IN `p_dni` INT, IN `p_nombre` VARCHAR(255), IN `p_apellido` VARCHAR(255), IN `p_telefono` INT, IN `p_email` VARCHAR(255))   BEGIN
    UPDATE Perfil
    SET dni = p_dni, nombre = p_nombre, apellido = p_apellido, telefono = p_telefono
    WHERE id = p_id;

    UPDATE Cliente
    SET email = p_email
    WHERE perfil_id = p_id;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `Administrativo`
--

CREATE TABLE `Administrativo` (
  `id` int(11) NOT NULL,
  `legajo` int(11) DEFAULT NULL,
  `perfil_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Autor`
--

CREATE TABLE `Autor` (
  `id` int(11) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `apellido` varchar(255) DEFAULT NULL,
  `nacionalidad` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Autor_Libro`
--

CREATE TABLE `Autor_Libro` (
  `autor_id` int(11) NOT NULL,
  `libro_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Cliente`
--

CREATE TABLE `Cliente` (
  `id` int(11) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `perfil_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Editorial`
--

CREATE TABLE `Editorial` (
  `id` int(11) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `telefono` int(11) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Editorial_Libro`
--

CREATE TABLE `Editorial_Libro` (
  `id` int(11) NOT NULL,
  `editorial_id` int(11) NOT NULL,
  `libro_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Instancia`
--

CREATE TABLE `Instancia` (
  `editorial_libro_id` int(11) NOT NULL,
  `id` int(11) NOT NULL,
  `fecha_entrada` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Libro`
--

CREATE TABLE `Libro` (
  `id` int(11) NOT NULL,
  `titulo` varchar(255) DEFAULT NULL,
  `edicion` varchar(255) DEFAULT NULL,
  `paginas` int(11) DEFAULT NULL,
  `lsbn` int(11) DEFAULT NULL,
  `genero` enum('FICCION','NO_FICCION','AVENTURA','ROMANCE','FANTASIA','TERROR','SUSPENSO','BIOGRAFIA','HISTORIA','CIENCIA','INFANTIL','POESIA','DRAMA','MISTERIO','AUTO_AYUDA') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Perfil`
--

CREATE TABLE `Perfil` (
  `id` int(11) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `apellido` varchar(255) DEFAULT NULL,
  `dni` int(11) DEFAULT NULL,
  `telefono` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Prestamo`
--

CREATE TABLE `Prestamo` (
  `id` int(11) NOT NULL,
  `cliente_id` int(11) NOT NULL,
  `fecha_inicio` date NOT NULL,
  `fecha_fin` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Prestamo_Libro`
--

CREATE TABLE `Prestamo_Libro` (
  `prestamo_id` int(11) NOT NULL,
  `instancia_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Administrativo`
--
ALTER TABLE `Administrativo`
  ADD PRIMARY KEY (`id`),
  ADD KEY `perfil_id` (`perfil_id`);

--
-- Indexes for table `Autor`
--
ALTER TABLE `Autor`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `Autor_Libro`
--
ALTER TABLE `Autor_Libro`
  ADD PRIMARY KEY (`autor_id`,`libro_id`),
  ADD KEY `libro_id` (`libro_id`);

--
-- Indexes for table `Cliente`
--
ALTER TABLE `Cliente`
  ADD PRIMARY KEY (`id`),
  ADD KEY `perfil_id` (`perfil_id`);

--
-- Indexes for table `Editorial`
--
ALTER TABLE `Editorial`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `Editorial_Libro`
--
ALTER TABLE `Editorial_Libro`
  ADD PRIMARY KEY (`editorial_id`,`libro_id`),
  ADD UNIQUE KEY `id` (`id`),
  ADD KEY `libro_id` (`libro_id`);

--
-- Indexes for table `Instancia`
--
ALTER TABLE `Instancia`
  ADD PRIMARY KEY (`id`),
  ADD KEY `editorial_libro_id` (`editorial_libro_id`);

--
-- Indexes for table `Libro`
--
ALTER TABLE `Libro`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `lsbn` (`lsbn`),
  ADD KEY `genero_id` (`genero`);

--
-- Indexes for table `Perfil`
--
ALTER TABLE `Perfil`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `dni` (`dni`);

--
-- Indexes for table `Prestamo`
--
ALTER TABLE `Prestamo`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`),
  ADD KEY `cliente_id` (`cliente_id`);

--
-- Indexes for table `Prestamo_Libro`
--
ALTER TABLE `Prestamo_Libro`
  ADD PRIMARY KEY (`prestamo_id`,`instancia_id`),
  ADD KEY `instancia_id` (`instancia_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Administrativo`
--
ALTER TABLE `Administrativo`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Autor`
--
ALTER TABLE `Autor`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Cliente`
--
ALTER TABLE `Cliente`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Editorial`
--
ALTER TABLE `Editorial`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Editorial_Libro`
--
ALTER TABLE `Editorial_Libro`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Instancia`
--
ALTER TABLE `Instancia`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Libro`
--
ALTER TABLE `Libro`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Perfil`
--
ALTER TABLE `Perfil`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Prestamo`
--
ALTER TABLE `Prestamo`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `Administrativo`
--
ALTER TABLE `Administrativo`
  ADD CONSTRAINT `Administrativo_ibfk_1` FOREIGN KEY (`perfil_id`) REFERENCES `Perfil` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Autor_Libro`
--
ALTER TABLE `Autor_Libro`
  ADD CONSTRAINT `Autor_Libro_ibfk_1` FOREIGN KEY (`autor_id`) REFERENCES `Autor` (`id`),
  ADD CONSTRAINT `Autor_Libro_ibfk_2` FOREIGN KEY (`libro_id`) REFERENCES `Libro` (`id`);

--
-- Constraints for table `Cliente`
--
ALTER TABLE `Cliente`
  ADD CONSTRAINT `Cliente_ibfk_1` FOREIGN KEY (`perfil_id`) REFERENCES `Perfil` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Editorial_Libro`
--
ALTER TABLE `Editorial_Libro`
  ADD CONSTRAINT `Editorial_Libro_ibfk_1` FOREIGN KEY (`editorial_id`) REFERENCES `Editorial` (`id`),
  ADD CONSTRAINT `Editorial_Libro_ibfk_2` FOREIGN KEY (`libro_id`) REFERENCES `Libro` (`id`);

--
-- Constraints for table `Instancia`
--
ALTER TABLE `Instancia`
  ADD CONSTRAINT `Instancia_ibfk_1` FOREIGN KEY (`editorial_libro_id`) REFERENCES `Editorial_Libro` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Prestamo`
--
ALTER TABLE `Prestamo`
  ADD CONSTRAINT `Prestamo_ibfk_1` FOREIGN KEY (`cliente_id`) REFERENCES `Cliente` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Prestamo_Libro`
--
ALTER TABLE `Prestamo_Libro`
  ADD CONSTRAINT `Prestamo_Libro_ibfk_2` FOREIGN KEY (`prestamo_id`) REFERENCES `Prestamo` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Prestamo_Libro_ibfk_3` FOREIGN KEY (`instancia_id`) REFERENCES `Instancia` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
