DROP TABLE IF EXISTS `user`, `course`, `role`, `theme`, `material`, `schedule`, `attendence`, `mark`, `admission`;

CREATE TABLE `user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `firstName` VARCHAR(45) collate utf8_bin NOT NULL,
  `secondName` VARCHAR(45) collate utf8_bin NOT NULL,
  `lastName` VARCHAR(45) collate utf8_bin NOT NULL,
  `birthDate` DATE NOT NULL,
  `login` VARCHAR(45) collate utf8_bin NOT NULL,
  `password` VARCHAR(76) collate utf8_bin NOT NULL,
  `id_role` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE `INDEX id_UNIQUE` (`id` ASC) VISIBLE);

CREATE TABLE `course` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45) collate utf8_bin NOT NULL,
  `description` MEDIUMTEXT collate utf8_bin NOT NULL,
  `hours` INT NOT NULL,
  `id_teacher` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE);

CREATE TABLE `role` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `role` VARCHAR(30) collate utf8_bin NOT NULL,
  `roleTitle` VARCHAR(30) collate utf8_bin NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UN_UNIQUE` (`role` ASC) VISIBLE,
                       UNIQUE INDEX `roleIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `roleTitle_UNIQUE` (`roleTitle` ASC) VISIBLE);

CREATE TABLE `theme` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(200) collate utf8_bin NOT NULL,
  `id_course` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE);

CREATE TABLE `material` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(100) collate utf8_bin NULL,
  `type` VARCHAR(20) collate utf8_bin NOT NULL,
  `path` MEDIUMTEXT collate utf8_bin NOT NULL,
  `id_theme` INT NOT NULL,
  `id_course` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE);

CREATE TABLE `schedule` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_theme` INT NOT NULL,
  `start_date` DATE,
  `end_date` DATE,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE);

CREATE TABLE `attendence` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_student` INT NOT NULL,
  `id_theme` INT NOT NULL,
  `attended` TINYINT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE);

CREATE TABLE `mark` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_student` INT NOT NULL,
  `id_lab` INT NOT NULL,
  `mark` INT NULL,
  `path` MEDIUMTEXT collate utf8_bin NULL,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  PRIMARY KEY (`id`));

CREATE TABLE `admission` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_student` INT NOT NULL,
  `id_course` INT NOT NULL,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `student_course` (`id_student` ASC, `id_course` ASC) VISIBLE);

ALTER TABLE `mark`
ADD UNIQUE INDEX `student_lab` (`id_student` ASC, `id_lab` ASC) VISIBLE;

ALTER TABLE `attendence`
ADD UNIQUE INDEX `student_theme` (`id_student` ASC, `id_theme` ASC) VISIBLE;

ALTER TABLE `schedule`
ADD UNIQUE INDEX `id_theme_UNIQUE` (`id_theme` ASC) VISIBLE;

ALTER TABLE `user`
ADD INDEX `user_role_idx` (`id_role` ASC) VISIBLE;
;
ALTER TABLE `user`
ADD CONSTRAINT `user_role`
  FOREIGN KEY (`id_role`)
  REFERENCES `role` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `course`
ADD INDEX `course_teacher_idx` (`id_teacher` ASC) VISIBLE;
;
ALTER TABLE `course`
ADD CONSTRAINT `course_teacher`
  FOREIGN KEY (`id_teacher`)
  REFERENCES user (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `theme`
ADD INDEX `theme_course_idx` (`id_course` ASC) VISIBLE;
;
ALTER TABLE `theme`
ADD CONSTRAINT `theme_course`
  FOREIGN KEY (`id_course`)
  REFERENCES `course` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `material`
ADD INDEX `material_theme_idx` (`id_theme` ASC) VISIBLE;
;
ALTER TABLE `material`
ADD CONSTRAINT `material_theme`
  FOREIGN KEY (`id_theme`)
  REFERENCES `theme` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

  ALTER TABLE `material`
  ADD CONSTRAINT `material_course`
    FOREIGN KEY (`id_course`)
    REFERENCES course (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION;

ALTER TABLE `schedule`
ADD CONSTRAINT `schedule_theme`
  FOREIGN KEY (`id_theme`)
  REFERENCES `theme` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `attendence`
ADD CONSTRAINT `attendence_student`
  FOREIGN KEY (`id_student`)
  REFERENCES user (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `attendence`
ADD INDEX `attendence_theme_idx` (`id_theme` ASC) VISIBLE;
;
ALTER TABLE `attendence`
ADD CONSTRAINT `attendence_theme`
  FOREIGN KEY (`id_theme`)
  REFERENCES `theme` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `mark`
ADD CONSTRAINT `mark_student`
  FOREIGN KEY (`id_student`)
  REFERENCES user (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `mark`
ADD INDEX `mark_lab_idx` (`id_lab` ASC) VISIBLE;
;
ALTER TABLE `mark`
ADD CONSTRAINT `mark_lab`
  FOREIGN KEY (`id_lab`)
  REFERENCES `material` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `admission`
ADD CONSTRAINT `admission_student`
  FOREIGN KEY (`id_student`)
  REFERENCES user (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `admission`
ADD INDEX `admission_course_idx` (`id_course` ASC) VISIBLE;
;
ALTER TABLE `admission`
ADD CONSTRAINT `admission_course`
  FOREIGN KEY (`id_course`)
  REFERENCES `course` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

