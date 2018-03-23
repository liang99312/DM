-- Database: "DocManamentDB"

CREATE DATABASE "DocManamentDB"
  WITH 
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'en_US.UTF-8'
       LC_CTYPE = 'en_US.UTF-8'
       CONNECTION LIMIT = -1;

CREATE TABLE public.t_bm
(
  qybh character(32),
  bh character(32),
  mc character varying(50),
  dm character(10),
  bmms character varying(250),
  zt integer
);

CREATE TABLE public.t_bmyg
(
  qybh character(32),
  bh character(32) NOT NULL,
  bmbh character(32),
  ygbh character(32),
  CONSTRAINT pk_t_bmyg PRIMARY KEY (bh)
);

CREATE TABLE public.t_dl
(
  ygbh character(32) NOT NULL,
  dlm character(32),
  dlmm character varying(16),
  CONSTRAINT pk_t_dl PRIMARY KEY (ygbh)
);

CREATE TABLE public.t_kh
(
  qybh character(32),
  bh character(32) NOT NULL,
  mc character varying(50),
  dm character varying(10),
  lxr character(32),
  lxfs character varying(20),
  zt integer,
  CONSTRAINT pk_t_kh PRIMARY KEY (bh)
);

CREATE TABLE public.t_qx
(
  qybh character(32),
  bh character(32) NOT NULL,
  wjflbh character(32),
  js character varying(32),
  jsdm character varying(32),
  bmbh character(32),
  cz character varying,
  bz character varying,
  CONSTRAINT pk_t_qx PRIMARY KEY (bh)
);

CREATE TABLE public.t_qy
(
  bh character(32) NOT NULL,
  mc character varying(50),
  sjhm character(11),
  CONSTRAINT pk_t_qy PRIMARY KEY (bh)
);

CREATE TABLE public.t_wj
(
  qybh character(32),
  bh character(32) NOT NULL,
  mc character varying(100),
  bmbh character(32),
  khbh character(32),
  wjflbh character(32),
  wjlx character(10),
  wjdx integer,
  zz character(20),
  scr character(32),
  scsj date,
  shr character(32),
  shbz character varying,
  shsj date,
  zlr character(32),
  zlsj date,
  zlbz character varying,
  jym character varying(32),
  gjz character varying(50),
  bz character varying,
  zt integer,
  CONSTRAINT pk_t_wj PRIMARY KEY (bh)
);

CREATE TABLE public.t_wj_temp
(
  qybh character(32),
  bh character(32),
  mc character varying(100),
  bmbh character(32),
  khbh character(32),
  wjflbh character(32),
  wjlx character(10),
  wjdx integer,
  zz character(32),
  scr character(32),
  scsj date,
  jym character varying(32),
  gjz character varying(50),
  bz character varying
);

CREATE TABLE public.t_wjfl
(
  qybh character(32),
  bh character(32) NOT NULL,
  mc character varying(20),
  dm character(10),
  jb character varying(20),
  bz character varying,
  zt integer,
  CONSTRAINT pk_t_wjfl PRIMARY KEY (bh)
);

CREATE TABLE public.t_wjlog
(
  qybh character(32),
  bh character(32) NOT NULL,
  wjbh character(32),
  cz character varying(20),
  czr character(32),
  czsj date,
  bz character varying,
  CONSTRAINT pk_t_wjlog PRIMARY KEY (bh)
);

CREATE TABLE public.t_yg
(
  qybh character(32),
  bh character(32) NOT NULL,
  mc character varying(20),
  dm character(6),
  xb character(10),
  sfzh character(18),
  sjhm character(11),
  rzsj date,
  zt integer,
  CONSTRAINT pk_t_yg PRIMARY KEY (bh)
);

CREATE TABLE public.t_ygjs
(
  bh character(32) NOT NULL,
  ygbh character(32),
  js character varying(20),
  jsdm character varying(10),
  CONSTRAINT pk_t_ygjs PRIMARY KEY (bh)
);
