package com.lang.social.logic;

public enum GameType {
    Timer, 
    LastStanding,
    QuizGame,
    PicGame,
    StudentTeacher,
    StudentGame,
    TeacherGame,
    HeadToHeadQuizGame,
    MemoryGame;
    
    @Override
    public String toString() {
      switch(this) {
        case Timer: return "Timer";
        case LastStanding: return "LastStanding";
        case HeadToHeadQuizGame: return "HeadToHeadQuizGame";
        case PicGame: return "PicGame";
        case QuizGame: return "QuizGame";
        case StudentTeacher: return "StudentTeacher";
        case StudentGame: return "StudentGame";
        case TeacherGame: return "TeacherGame";
        case MemoryGame: return "MemoryGame";
        default: throw new IllegalArgumentException();
      }
    }
   
    
}
