package de.retest.recheck.review.ignore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import de.retest.recheck.ui.descriptors.Attributes;
import de.retest.recheck.ui.descriptors.Element;
import de.retest.recheck.ui.diff.AttributeDifference;

class RegexAttributeShouldIgnoreTest {

	@Test
	void should_match_wildcard_attribute() {
		final AttributeRegexShouldIgnore shouldIgnore = new AttributeRegexShouldIgnore( ".*Special.*" );

		final Element element = mock( Element.class );
		final Attributes attribs = mock( Attributes.class );
		when( element.getAttributes() ).thenReturn( attribs );
		when( attribs.get( "mySpecialAttribute" ) ).thenReturn( "someValue" );

		final AttributeDifference attributeDifference = mock( AttributeDifference.class );
		when( attributeDifference.getKey() ).thenReturn( "mySpecialAttribute" );

		assertThat( shouldIgnore.shouldIgnoreAttributeDifference( element, attributeDifference ) ).isTrue();
	}

}
