DROP TABLE IF EXISTS solicitud;
DROP TABLE IF EXISTS estados;
DROP TABLE IF EXISTS tipo_prestamo;

CREATE TABLE estados (
                         id_estado BIGINT PRIMARY KEY AUTO_INCREMENT,
                         nombre VARCHAR(255) NOT NULL UNIQUE,
                         descripcion VARCHAR(255) NOT NULL
);

CREATE TABLE tipo_prestamo (
                               id_tipo_prestamo BIGINT PRIMARY KEY AUTO_INCREMENT,
                               nombre VARCHAR(255) NOT NULL,
                               monto_minimo DECIMAL(15, 2) NOT NULL,
                               monto_maximo DECIMAL(15, 2) NOT NULL,
                               tasa_interes DECIMAL(5, 4) NOT NULL,
                               validacion_automatica BOOLEAN NOT NULL
);

CREATE TABLE solicitud (
                           id_solicitud BIGINT PRIMARY KEY AUTO_INCREMENT,
                           monto DECIMAL(15, 2) NOT NULL,
                           plazo INT NOT NULL,
                           email VARCHAR(255) NOT NULL,
                           id_estado BIGINT NOT NULL,
                           id_tipo_prestamo BIGINT NOT NULL,
                           FOREIGN KEY (id_estado) REFERENCES estados(id_estado),
                           FOREIGN KEY (id_tipo_prestamo) REFERENCES tipo_prestamo(id_tipo_prestamo)
);

INSERT INTO estados (nombre, descripcion) VALUES ('PENDIENTE_REVISION', 'La solicitud está pendiente de ser revisada por un administrador.');
INSERT INTO tipo_prestamo (id_tipo_prestamo, nombre, monto_minimo, monto_maximo, tasa_interes, validacion_automatica) VALUES (1, 'Personal Express', 1000.00, 10000.00, 0.15, true);
