package rakuproject.raku.domain.move.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rakuproject.raku.domain.move.entity.ReactionEntity;
import rakuproject.raku.domain.move.service.ReactionService;

import java.util.List;

@RestController
@RequestMapping("/move/reactions")
@CrossOrigin(origins = "http://localhost:3000")
public class ReactionController {
    private final ReactionService reactionService;

    public ReactionController(ReactionService reactionService) {
        this.reactionService = reactionService;
    }

    @GetMapping
    public ResponseEntity<List<ReactionEntity>> getAllReactions() {
        List<ReactionEntity> reactions = reactionService.getAllReactions();
        return ResponseEntity.ok(reactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReactionEntity> getReactionById(@PathVariable Long id) {
        ReactionEntity reaction = reactionService.getReactionById(id);
        return ResponseEntity.ok(reaction);
    }

    @PostMapping
    public ResponseEntity<ReactionEntity> createReaction(@RequestBody ReactionEntity reaction) {
        ReactionEntity createdReaction = reactionService.createReaction(reaction);
        return ResponseEntity.ok(createdReaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReactionEntity> updateReaction(@PathVariable Long id, @RequestBody ReactionEntity reaction) {
        ReactionEntity updatedReaction = reactionService.updateReaction(id, reaction);
        return ResponseEntity.ok(updatedReaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReaction(@PathVariable Long id) {
        reactionService.deleteReaction(id);
        return ResponseEntity.noContent().build();
    }
}

