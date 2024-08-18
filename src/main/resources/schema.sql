CREATE TABLE IF NOT EXISTS employees (
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    employee_id character varying(15) NOT NULL UNIQUE,
    first_name character varying(50) NOT NULL,
    last_name character varying(50) NOT NULL,
    email character varying(50) NOT NULL,
    phone_numbers text[] NOT NULL,
    doj timestamp without time zone NOT NULL,
    salary double precision NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    full_name character varying(50) NOT NULL,
    email character varying(50) NOT NULL UNIQUE,
    password character varying NOT NULL
);