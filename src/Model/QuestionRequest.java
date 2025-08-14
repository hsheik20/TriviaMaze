package Model;

import java.io.Serializable;

public record QuestionRequest(Door door, Question question) implements Serializable {

}
