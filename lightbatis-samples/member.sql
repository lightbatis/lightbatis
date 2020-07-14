/*
 Navicat Premium Data Transfer

 Source Server         : localhost_postgres
 Source Server Type    : PostgreSQL
 Source Server Version : 110004
 Source Host           : localhost:5432
 Source Catalog        : demo
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 110004
 File Encoding         : 65001

 Date: 13/07/2020 14:29:24
*/


-- ----------------------------
-- Table structure for member
-- ----------------------------
DROP TABLE IF EXISTS "public"."member";
CREATE TABLE "public"."member" (
  "id" int8 NOT NULL,
  "created_by" int8,
  "created_time" timestamp(6),
  "kind_id" int4,
  "member_name" varchar(255) COLLATE "pg_catalog"."default",
  "revision" int4,
  "staff_id" int8,
  "staff_name" varchar(255) COLLATE "pg_catalog"."default",
  "status" int4,
  "update_by" int8,
  "update_time" date,
  "verify_status" varchar(255) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "public"."member" OWNER TO "postgres";

-- ----------------------------
-- Primary Key structure for table member
-- ----------------------------
ALTER TABLE "public"."member" ADD CONSTRAINT "member_pkey" PRIMARY KEY ("id");
