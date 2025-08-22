package Model;

import java.io.Serializable;

/**
 * A record to encapsulate a request for a question, associating a specific
 * {@link Door} with a {@link Question}.
 * This record is immutable and is designed for use in situations where
 * data needs to be easily passed between components, such as when
 * requesting a question related to a specific door in a game.
 *
 * @param door     The {@link Door} for which the question is being requested.
 * @param question The {@link Question} that is presented with the door.
 *
 * @author Husein
 */
public record QuestionRequest(Door door, Question question) implements Serializable {

}