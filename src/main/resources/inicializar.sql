DROP TABLE IF EXISTS public.cart_items;
DROP TABLE IF EXISTS public.carts;
DROP TABLE IF EXISTS public.order_items;
DROP TABLE IF EXISTS public.orders;
DROP TABLE IF EXISTS public.games;
DROP TABLE IF EXISTS public.categories;
DROP TABLE IF EXISTS public.users;
DROP TABLE IF EXISTS public.status;
DROP TABLE IF EXISTS public.roles;

CREATE TABLE IF NOT EXISTS public.roles (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(12) UNIQUE NOT NULL,
    description VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS public.users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(40) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    role_id BIGINT NOT NULL,
    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES public.roles(id)
);

CREATE TABLE IF NOT EXISTS public.categories (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(20) UNIQUE NOT NULL,
    description VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS public.games (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(30) NOT NULL,
    description VARCHAR(100) NOT NULL,
    price DECIMAL(7, 2) NOT NULL,
    stock INTEGER NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    category_id BIGINT NOT NULL,
    CONSTRAINT fk_game_category FOREIGN KEY (category_id) REFERENCES public.categories(id)
);

CREATE TABLE IF NOT EXISTS public.status (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(15) UNIQUE NOT NULL,
    description VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS public.orders (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    quantity INTEGER NOT NULL,
    total DECIMAL(8, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    user_id BIGINT NOT NULL,
    status_id BIGINT NOT NULL,
    CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES public.users(id),
    CONSTRAINT fk_order_status FOREIGN KEY (status_id) REFERENCES public.status(id)
);

CREATE TABLE IF NOT EXISTS public.order_items (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    quantity INTEGER NOT NULL,
    price DECIMAL(7, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    order_id BIGINT NOT NULL,
    game_id BIGINT NOT NULL,
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES public.orders(id),
    CONSTRAINT fk_order_item_game FOREIGN KEY (game_id) REFERENCES public.games(id)
);

CREATE TABLE IF NOT EXISTS public.carts (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    user_id BIGINT UNIQUE NOT NULL,
    subtotal DECIMAL(8, 2) NOT NULL DEFAULT 0,
    CONSTRAINT fk_cart_user FOREIGN KEY (user_id)
    REFERENCES public.users(id)
);

CREATE TABLE IF NOT EXISTS public.cart_items (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    quantity INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    cart_id BIGINT NOT NULL,
    game_id BIGINT NOT NULL,
    CONSTRAINT fk_cart_item_cart
    FOREIGN KEY (cart_id) REFERENCES public.carts(id),
    CONSTRAINT fk_cart_item_game
    FOREIGN KEY (game_id) REFERENCES public.games(id),
    CONSTRAINT uq_cart_game UNIQUE (cart_id, game_id)
);


INSERT INTO public.roles (name, description) VALUES ('ADMIN', 'Administrador do sistema');

INSERT INTO public.users (name, email, password, phone, role_id)
SELECT 'Admin', 'admin@admin.com', 'admin123', '00000000000', id
FROM public.roles WHERE name = 'ADMIN';

INSERT INTO public.carts (user_id)
SELECT id FROM public.users WHERE email = 'admin@admin.com';

INSERT INTO public.categories (name, description) VALUES
    ('Ação', 'Jogos de ação e combate'),
    ('Aventura', 'Jogos de exploração e aventura'),
    ('RPG', 'Jogos de interpretação de papéis'),
    ('Esporte', 'Jogos esportivos'),
    ('Estratégia', 'Jogos de estratégia e planejamento');

INSERT INTO public.status (name, description) VALUES
    ('PENDENTE',    'Pedido aguardando pagamento'),
    ('PAGO',        'Pagamento confirmado'),
    ('ENVIADO',     'Pedido enviado ao cliente'),
    ('CANCELADO',   'Pedido cancelado');
