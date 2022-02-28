package com.marketlogicsoftware.pollingservice.controller;

import com.marketlogicsoftware.pollingservice.model.audit.AppError;
import com.marketlogicsoftware.pollingservice.payload.*;
import com.marketlogicsoftware.pollingservice.security.CurrentUser;
import com.marketlogicsoftware.pollingservice.security.UserPrincipal;
import com.marketlogicsoftware.pollingservice.service.PollService;
import com.marketlogicsoftware.pollingservice.util.AppConstants;
import com.marketlogicsoftware.pollingservice.model.*;
import com.marketlogicsoftware.pollingservice.repository.PollRepository;
import com.marketlogicsoftware.pollingservice.repository.UserRepository;
import com.marketlogicsoftware.pollingservice.repository.VoteRepository;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/polls")
@Api(value = "User Rest Controller", description = "REST API for User")
public class PollController {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PollService pollService;

    private static final Logger logger = LoggerFactory.getLogger(PollController.class);

    @GetMapping
    public PagedResponse<PollResponse> getPolls(@CurrentUser UserPrincipal currentUser,
                                                @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return pollService.getAllPolls(currentUser, page, size);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createPoll(@Valid @RequestBody PollRequest pollRequest) {
        Poll poll = pollService.createPoll(pollRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{pollId}")
                .buildAndExpand(poll.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Poll Created Successfully"));
    }


    @PostMapping("/{pollId}")
    public PollResponse getPollById(@CurrentUser UserPrincipal currentUser,
                                    @PathVariable Long pollId) {
        PollResponse pollResponse = null;
        try {
            pollResponse = pollService.getPollById(pollId, currentUser);
        } catch (Exception ex) {
            new AppError(ex.getMessage());
        }
        return pollResponse;
    }

    @PostMapping("/{pollId}/votes")
    @PreAuthorize("hasRole('USER')")
    public PollResponse castVote(@CurrentUser UserPrincipal currentUser,
                         @PathVariable Long pollId,
                         @Valid @RequestBody VoteRequest voteRequest) {

        PollResponse pollResponse = null;
        try {
             pollResponse = pollService.castVoteAndGetUpdatedPoll(pollId, voteRequest, currentUser);
        } catch (Exception ex) {
            new AppError(ex.getMessage());
        }
        return pollResponse;
    }

}
