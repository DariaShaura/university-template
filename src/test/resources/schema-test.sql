create table user (id bigint not null AUTO_INCREMENT, firstName VARCHAR(45), secondName VARCHAR(45), lastName VARCHAR(45), birthDate DATE NOT NULL, login VARCHAR(45), password VARCHAR(76), id_role INT NOT NULL, PRIMARY KEY (id))

CREATE TABLE `role` (`id` INT NOT NULL AUTO_INCREMENT, `role` VARCHAR(30), `roleTitle` VARCHAR(30), PRIMARY KEY (`id`))

CREATE TABLE `course` (`id` INT NOT NULL AUTO_INCREMENT, `title` VARCHAR(45) NOT NULL, `description` MEDIUMTEXT NOT NULL, `hours` INT NOT NULL, `id_teacher` INT NOT NULL, PRIMARY KEY (`id`))

CREATE TABLE `theme` (`id` INT NOT NULL AUTO_INCREMENT, `title` VARCHAR(200) NOT NULL, `id_course` INT NOT NULL, PRIMARY KEY (`id`))

CREATE TABLE `material` ( `id` INT NOT NULL AUTO_INCREMENT, `title` VARCHAR(100) NULL, `type` VARCHAR(20) NOT NULL, `path` MEDIUMTEXT NOT NULL, `id_theme` INT NOT NULL, `id_course` INT NOT NULL, PRIMARY KEY (`id`))

CREATE TABLE `schedule` (`id` INT NOT NULL AUTO_INCREMENT, `id_theme` INT NOT NULL, `start_date` DATE, `end_date` DATE, PRIMARY KEY (`id`))

CREATE TABLE `attendence` (`id` INT NOT NULL AUTO_INCREMENT, `id_student` INT NOT NULL, `id_theme` INT NOT NULL, `attended` TINYINT NOT NULL, PRIMARY KEY (`id`))

CREATE TABLE `mark` (`id` INT NOT NULL AUTO_INCREMENT, `id_student` INT NOT NULL, `id_lab` INT NOT NULL, `mark` INT NULL, `path` MEDIUMTEXT NULL, PRIMARY KEY (`id`))

CREATE TABLE `admission` (`id` INT NOT NULL AUTO_INCREMENT, `id_student` INT NOT NULL, `id_course` INT NOT NULL, PRIMARY KEY (`id`))

ALTER TABLE `mark` ADD UNIQUE INDEX `student_lab` (`id_student` ASC, `id_lab` ASC)

ALTER TABLE `attendence` ADD UNIQUE INDEX `student_theme` (`id_student` ASC, `id_theme` ASC)

ALTER TABLE `schedule` ADD UNIQUE INDEX `id_theme_UNIQUE` (`id_theme` ASC)

ALTER TABLE `user` ADD CONSTRAINT `user_role` FOREIGN KEY (`id_role`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION

ALTER TABLE `course` ADD CONSTRAINT `course_teacher` FOREIGN KEY (`id_teacher`) REFERENCES user (`id`) ON DELETE CASCADE ON UPDATE NO ACTION

ALTER TABLE `theme` ADD CONSTRAINT `theme_course` FOREIGN KEY (`id_course`) REFERENCES `course` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION

ALTER TABLE `material` ADD CONSTRAINT `material_theme` FOREIGN KEY (`id_theme`) REFERENCES `theme` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION

ALTER TABLE `material` ADD CONSTRAINT `material_course` FOREIGN KEY (`id_course`) REFERENCES `course` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION

ALTER TABLE `schedule` ADD CONSTRAINT `schedule_theme` FOREIGN KEY (`id_theme`) REFERENCES `theme` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION

ALTER TABLE `attendence` ADD CONSTRAINT `attendence_student` FOREIGN KEY (`id_student`) REFERENCES user (`id`) ON DELETE CASCADE ON UPDATE NO ACTION

ALTER TABLE `attendence` ADD CONSTRAINT `attendence_theme` FOREIGN KEY (`id_theme`) REFERENCES `theme` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION

ALTER TABLE `mark` ADD CONSTRAINT `mark_student` FOREIGN KEY (`id_student`) REFERENCES user (`id`) ON DELETE CASCADE ON UPDATE NO ACTION

ALTER TABLE `mark` ADD CONSTRAINT `mark_lab` FOREIGN KEY (`id_lab`) REFERENCES `material` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION

ALTER TABLE `admission` ADD CONSTRAINT `admission_student` FOREIGN KEY (`id_student`) REFERENCES user (`id`) ON DELETE CASCADE ON UPDATE NO ACTION

ALTER TABLE `admission` ADD CONSTRAINT `admission_course` FOREIGN KEY (`id_course`) REFERENCES `course` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
