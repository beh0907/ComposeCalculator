package com.skymilk.composecalculator

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

    fun <T> ArrayDeque<T>.push(element: T) = addLast(element)
    fun <T> ArrayDeque<T>.pop() = removeLastOrNull()
    fun <T> ArrayDeque<T>.peek() = lastOrNull()
}