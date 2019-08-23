package ru.skillbranch.devintensive.models

class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME){

    fun askQuestion(): String = when(question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String) : Pair<String, Triple<Int, Int, Int>> {
        val(isValid, message) = question.validate(answer)
        if(!isValid) return "$message\n${question.question}" to status.color

        if (question == Question.IDLE) return "Отлично - ты справился\n${question.question}" to status.color

        if(question.answers.contains(answer.toLowerCase())) {
            question = question.nextQuestion()
            return "Отлично - ты справился\n${question.question}" to status.color
        }else {
            status = status.nextStatus()
            if (status != Status.NORMAL) {
                return "Это неправильный ответ\n${question.question}" to status.color
            } else {
                question = Question.NAME
                return "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
            }
        }
    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)) ,
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0)) ;

        fun nextStatus(): Status {
            return if(this.ordinal < values().lastIndex) {
                values()[this.ordinal + 1]
            } else {
                values()[0]
            }
        }
    }

    enum class Question(val question: String, val answers: List<String>) {
        NAME("Как меня зовут?", listOf("бендер", "bender")) {
            override fun nextQuestion(): Question = PROFESSION
            override fun getErrorMessage() = "Имя должно начинаться с заглавной буквы"
            override fun getValidator() = "^[A-Z](.*?)".toRegex()
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")) {
            override fun nextQuestion(): Question = MATERIAL
            override fun getErrorMessage() = "Профессия должна начинаться со строчной буквы"
            override fun getValidator() = "^[a-z](.*?)".toRegex()
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")) {
            override fun nextQuestion(): Question = BDAY
            override fun getErrorMessage() = "Материал не должен содержать цифр"
            override fun getValidator() = "[A-Za-z]+".toRegex()
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun nextQuestion(): Question = SERIAL
            override fun getErrorMessage() = "Год моего рождения должен содержать только цифры"
            override fun getValidator() = "[0-9]+".toRegex()
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun nextQuestion(): Question = IDLE
            override fun getErrorMessage() = "Серийный номер содержит только цифры, и их 7"
            override fun getValidator() = "^[0-9]*7".toRegex()
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun nextQuestion(): Question = IDLE
            override fun getErrorMessage() = ""
            override fun getValidator() = "".toRegex()
        };

        abstract fun nextQuestion() : Question
        abstract fun getValidator() : Regex
        abstract fun getErrorMessage() : String

        fun validate(answer:String) : Pair<Boolean, String> = Pair(getValidator().matches(answer), getErrorMessage())
    }
}