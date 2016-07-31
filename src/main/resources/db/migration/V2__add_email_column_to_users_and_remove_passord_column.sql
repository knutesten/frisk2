ALTER TABLE "user" ADD email varchar(255) UNIQUE;
ALTER TABLE "user" DROP password;
UPDATE "user" SET email = 'knut.neksa@gmail.com' WHERE id = 1;