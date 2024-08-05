package com.skymilk.composecalculator

import kotlin.math.pow


fun <T> ArrayDeque<T>.push(element: T) = addLast(element)
fun <T> ArrayDeque<T>.pop() = removeLastOrNull()
fun <T> ArrayDeque<T>.peek() = lastOrNull()

class InfixToPostFix {

    //숫자 여부 체크
    private fun notNumeric(ch: Char): Boolean = when (ch) {
        '+', '-', '*', '/', '(', ')', '^' -> true
        else -> false
    }

    //연산자 우선순위
    private fun operatorPrecedence(ch: Char): Int = when (ch) {
        '+', '-' -> 1
        '*', '/' -> 2
        '^' -> 3
        else -> -1
    }

    //중위표기법 -> 후위표기법으로 변환
    fun postFixConversion(string: String): String {
        var result = ""

        val deque = ArrayDeque<Char>()

        for (str in string) {
            if (!notNumeric(str)) {// 숫자라면 값을 저장
                result += str
            } else if (str == '(') {// 괄호를 여는 경우 추가
                deque.push(str)
            } else if (str == ')') {//괄호를 닫는 경우

                //큐에 담긴 모든 정보를 저장한다
                while (!deque.isEmpty() && deque.peek() != '(') {
                    result += " " + deque.pop()
                }
                deque.pop()
            } else { //연산자일 경우
                while (!deque.isEmpty() && operatorPrecedence(str) <= operatorPrecedence(deque.peek()!!)) {
                    result += " ${deque.pop()} "
                }

                deque.push(str)
                result += " "
            }
        }

        result += " "
        while (!deque.isEmpty()) {
            //여는 괄호가 남아있다면 오류
            if (deque.peek() == '(') return "Error"

            result += deque.pop()!! + " "
        }

        return result.trim()
    }
}

class Model {

    //음수 처리
    private fun replaceN(string: String): String {
        val array = StringBuffer(string)

        //첫번쨰 음수를 N으로 표기한다
        if (array[0] == '-') {
            array.setCharAt(0, 'n')
        }

        var i = 0
        while (i < array.length) {

            if (array[i] == '-') {
                //현재 - 기호가 있으며 이전에 위치에 연산자가 있다면 음수로 인식
                if (array[i - 1] == '+' ||
                    array[i - 1] == '-' ||
                    array[i - 1] == '*' ||
                    array[i - 1] == '/' ||
                    array[i - 1] == '('
                ) {
                    array.setCharAt(i, 'n')
                }
            }

            i++
        }

        return array.toString()
    }

    fun getResult(string: String): String {
        val stringN = replaceN(string) // 음수 표시를 위해 N 변환
        val postFix = InfixToPostFix().postFixConversion(stringN) // 중위표기 -> 후위표기로 변환

        if (postFix == "Error") return postFix

        return try {
            val evaluation = ArithmeticEvaluation().evaluation(postFix)
            evaluation.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            "Error"
        }
    }
}

class ArithmeticEvaluation {
    //연산자 여부 체크
    private fun notOperator(ch: Char): Boolean = when (ch) {
        '+', '-', '*', '/', '(', ')', '^' -> false
        else -> true
    }

    //계산
    fun evaluation(string: String): Double {
        var value = ""

        val deque = ArrayDeque<Double>()

        for (ch in string) {
            //연산자도 아니며 공백도 아니라면 (숫자)
            if (notOperator(ch) && ch != ' ') {
                value += ch
            } else if (ch == ' ' && value != "") {
                //음수 표시를 위해 다시 -로 변환하여 큐에 삽입
                deque.push(value.replace('n', '-').toDouble())

                value = ""
            } else if (!notOperator(ch)) {
                //연산자라면 저장된 값 2개를 큐로부터 가져온다
                val pop1 = deque.pop()
                val pop2 = deque.pop()

                //두 값을 연산자로 계산하여 큐에 추가한다
                when (ch) {
                    '+' -> deque.push(pop2!! + pop1!!)
                    '-' -> deque.push(pop2!! - pop1!!)
                    '*' -> deque.push(pop2!! * pop1!!)
                    '/' -> deque.push(pop2!! / pop1!!)
                    '^' -> deque.push(pop2!!.pow(pop1!!))
                }
            }
        }

        //큐에 마지막 남은 값이 결과가 된다
        return deque.pop()!!
    }
}