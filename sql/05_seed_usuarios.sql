DELETE FROM usuario;

INSERT INTO usuario (nome, email, senha, role, ativo) VALUES
('Administrador', 'admin@nextlog.com',     '$2a$12$YMQx.0jk4pQGU3vk7CcUEOcq3IbSR93tUOJWTY.ofwzPGj8BeQMly', 'ADMIN',     true),
('Gestor Teste',  'gestor@nextlog.com',    '$2a$12$YMQx.0jk4pQGU3vk7CcUEOcq3IbSR93tUOJWTY.ofwzPGj8BeQMly', 'GESTOR',    true),
('Motorista Teste','motorista@nextlog.com','$2a$12$YMQx.0jk4pQGU3vk7CcUEOcq3IbSR93tUOJWTY.ofwzPGj8BeQMly', 'MOTORISTA', true);

UPDATE motorista
SET id_usuario = (SELECT id FROM usuario WHERE email = 'motorista@nextlog.com')
WHERE id = 1;