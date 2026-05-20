package com.simpleblog.backend.me;

import com.simpleblog.backend.auth.AuthenticatedUser;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/me")
public class MyProfileController {

    private final MyProfileService myProfileService;

    public MyProfileController(MyProfileService myProfileService) {
        this.myProfileService = myProfileService;
    }

    @GetMapping
    public MyProfileResponse getProfile(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return myProfileService.getProfile(authenticatedUser);
    }

    @GetMapping("/posts")
    public List<MyPostSummaryResponse> getMyPosts(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return myProfileService.getMyPosts(authenticatedUser);
    }

    @GetMapping("/posts/{postId}")
    public UpdateMyPostRequest getMyPostForEdit(@PathVariable Long postId,
                                                @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return myProfileService.getMyPostForEdit(postId, authenticatedUser);
    }

    @PutMapping("/posts/{postId}")
    public MyPostSummaryResponse updateMyPost(@PathVariable Long postId,
                                              @Valid @RequestBody UpdateMyPostRequest request,
                                              @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return myProfileService.updateMyPost(postId, request, authenticatedUser);
    }

    @DeleteMapping("/posts/{postId}")
    public MyPostSummaryResponse hideMyPost(@PathVariable Long postId,
                                            @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return myProfileService.hideMyPost(postId, authenticatedUser);
    }
}
