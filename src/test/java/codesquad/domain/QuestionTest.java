package codesquad.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class QuestionTest {

    private User user;
    private Question question;

    @Before
    public void setUp() throws Exception {
        user = new User("java", "1234", "pobi", "a@b.com");
        user.setId(1L);
        question = new Question(user, "title", "contents");
        question.setId(1L);
    }

    @Test
    public void updateSucceed() {
        User testUser = new User("java", "1234", "pobi", "a@b.com");
        testUser.setId(1L);
        Question testQuestion = new Question(testUser, "title", "contents");
        testQuestion.setId(1L);
        question.update(testQuestion);
        assertEquals(question, testQuestion);
    }

    @Test
    public void updateFail_다른질문id() {
        Question questionTest = new Question(new User(), "titleTest", "contentTest");
        questionTest.setId(-1L);
        question.update(questionTest);
        assertNotEquals(question, questionTest);
    }

    @Test
    public void updateFail_다른작성자id() {
        User testUser = new User("java", "1234", "pobi", "a@b.com");
        testUser.setId(-1L);
        Question questionTest = new Question(testUser, "titleTest", "contentTest");
        questionTest.setId(1L);
        question.update(questionTest);
        assertNotEquals(question, questionTest);
    }

    @Test
    public void matchId_동일Id() {
        Question testQuestion = new Question(new User(), "", "");
        testQuestion.setId(1L);
        assertEquals(true, question.matchId(testQuestion));
    }

    @Test
    public void matchId_다른Id() {
        Question testQuestion = new Question(new User(), "", "");
        testQuestion.setId(-1L);
        assertEquals(false, question.matchId(testQuestion));
    }

    @Test
    public void matchWriter_동일Writer() {
        assertEquals(true, question.matchWriter(1L));
    }

    @Test
    public void matchWriter_다른Writer() {
        assertEquals(false, question.matchWriter(-1L));
    }

    @Test
    public void hasAnswerFalse() {
        question.setAnswers(new ArrayList<>());
        assertEquals(false, question.hasAnswer());
    }

    @Test
    public void hasAnswerTrue() {
        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer());
        question.setAnswers(answers);
        assertEquals(true, question.hasAnswer());
    }

    @Test
    public void matchQuestionWriterAndAllAnswerWriter_성공() {
        User testUser = new User();
        testUser.setId(1L);
        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer(null, testUser, null));
        question.setAnswers(answers);
        assertEquals(true, question.matchQuestionWriterAndAllAnswerWriter());
    }

    @Test
    public void matchQuestionWriterAndAllAnswerWriter_실패() {
        User testUser = new User();
        testUser.setId(2L);
        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer(null, testUser, null));
        question.setAnswers(answers);
        assertEquals(false, question.matchQuestionWriterAndAllAnswerWriter());
    }

    @Test
    public void canDelete_성공_answer_없는경우() {
        question.setAnswers(new ArrayList<>());
        assertEquals(true, question.canDelete(1L));
    }

    @Test
    public void canDelete_성공_모든_answerWriter가_questionWriter와_동일() {
        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer(null, user, null));
        question.setAnswers(answers);
        assertEquals(true, question.canDelete(1L));
    }

    @Test
    public void canDelete_실패_로그인사용자와_질문작성자가_다름() {
        Long testUserId = 2L;
        assertEquals(false, question.canDelete(testUserId));
    }

    @Test
    public void canDelete_실패_answerWriter가_questionWriter가_아님() {
        Long questionWriterId = 1L;
        Long answerWriterId = 2L;
        User answerWriter = new User();
        answerWriter.setId(answerWriterId);
        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer(null, answerWriter, null));
        question.setAnswers(answers);
        assertEquals(false, question.canDelete(questionWriterId));
    }

    @Test
    public void delete(){
        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer());
        answers.add(new Answer());
        question.setAnswers(answers);

        question.delete();

        boolean isDeletedAllAnswer = false;
        List<Answer> actualAnswers = question.getAnswers();
        if (actualAnswers.size() == actualAnswers.stream()
                                                .filter(answer -> answer.isDeleted())
                                                .count())
        {
            isDeletedAllAnswer = true;
        }

        assertEquals(true , (question.isDeleted() && isDeletedAllAnswer));
    }
}