package com.example.demo.dto.talkRelatedDTO;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TalkTalk {
	Talk myTalk;
	List<Talk> relatedTalk;
}
