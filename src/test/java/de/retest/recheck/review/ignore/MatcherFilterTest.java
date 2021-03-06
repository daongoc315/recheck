package de.retest.recheck.review.ignore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.retest.recheck.ignore.Filter;
import de.retest.recheck.review.ignore.matcher.ElementRetestIdMatcher;
import de.retest.recheck.ui.Path;
import de.retest.recheck.ui.descriptors.Element;
import de.retest.recheck.ui.descriptors.IdentifyingAttributes;
import de.retest.recheck.ui.descriptors.MutableAttributes;
import de.retest.recheck.ui.descriptors.RootElement;
import de.retest.recheck.ui.diff.AttributeDifference;

class MatcherFilterTest {

	MatcherFilter cut;

	@BeforeEach
	void setUp() {
		final Element element = mock( Element.class );
		when( element.getRetestId() ).thenReturn( "abc" );
		final ElementRetestIdMatcher matcher = new ElementRetestIdMatcher( element );
		cut = new MatcherFilter( matcher );
	}

	@Test
	void matches_should_accept_element() {
		final Element element = mock( Element.class );
		when( element.getRetestId() ).thenReturn( "abc" );

		assertThat( cut.matches( element ) ).isTrue();
	}

	@Test
	void matches_should_reject_element() {
		final Element element = mock( Element.class );
		when( element.getRetestId() ).thenReturn( "ABC" );

		assertThat( cut.matches( element ) ).isFalse();
	}

	@Test
	void matches_diff_should_match_when_element_does() {
		final Element element = mock( Element.class );
		when( element.getRetestId() ).thenReturn( "abc" );
		final AttributeDifference difference = mock( AttributeDifference.class );

		assertThat( cut.matches( element, difference ) ).isTrue();
	}

	@Test
	void matches_diff_should_not_match_when_element_does_not() {
		final Element element = mock( Element.class );
		when( element.getRetestId() ).thenReturn( "cba" );
		final AttributeDifference difference = mock( AttributeDifference.class );

		assertThat( cut.matches( element, difference ) ).isFalse();
	}

	@Test
	void matches_should_match_when_id_is_parent() {
		final Element childElement = mock( Element.class );
		when( childElement.getRetestId() ).thenReturn( "childabc" );

		final Element element = mock( Element.class );
		when( element.getRetestId() ).thenReturn( "abc" );

		when( childElement.getParent() ).thenReturn( element );

		assertThat( cut.matches( childElement ) ).isTrue();
	}

	@Test
	void matches_should_match_when_id_is_parent_of_parent() {
		final Element childElement = mock( Element.class );
		when( childElement.getRetestId() ).thenReturn( "childabc" );

		final Element element = mock( Element.class );
		when( element.getRetestId() ).thenReturn( "normalabc" );

		final Element parentElement = mock( Element.class );
		when( parentElement.getRetestId() ).thenReturn( "abc" );

		when( childElement.getParent() ).thenReturn( element );
		when( element.getParent() ).thenReturn( parentElement );

		assertThat( cut.matches( childElement ) ).isTrue();
	}

	@Test
	void matches_diff_should_match_when_id_is_parent() {
		final Element childElement = mock( Element.class );
		when( childElement.getRetestId() ).thenReturn( "childabc" );

		final Element element = mock( Element.class );
		when( element.getRetestId() ).thenReturn( "abc" );

		when( childElement.getParent() ).thenReturn( element );

		final AttributeDifference difference = mock( AttributeDifference.class );

		assertThat( cut.matches( childElement, difference ) ).isTrue();
	}

	@Test
	void matches_diff_should_match_when_id_is_parent_of_parent() {
		final Element childElement = mock( Element.class );
		when( childElement.getRetestId() ).thenReturn( "childabc" );

		final Element element = mock( Element.class );
		when( element.getRetestId() ).thenReturn( "normalabc" );

		final Element parentElement = mock( Element.class );
		when( parentElement.getRetestId() ).thenReturn( "abc" );

		when( childElement.getParent() ).thenReturn( element );
		when( element.getParent() ).thenReturn( parentElement );

		final AttributeDifference difference = mock( AttributeDifference.class );

		assertThat( cut.matches( childElement, difference ) ).isTrue();
	}

	@Test
	void should_match_when_expression_contains_whitespace() {
		final Element div = Element.create( "divId",
				new RootElement( "retestId", IdentifyingAttributes.create( Path.fromString( "html" ), "html" ),
						new MutableAttributes().immutable(), null, "screen", -1, "title" ),
				IdentifyingAttributes.create( Path.fromString( "html/div" ), "div" ),
				new MutableAttributes().immutable() );

		final Filter cut = new MatcherFilter.MatcherFilterLoader()
				.load( "matcher: xpath=/html/div, attribute= mySpecialAttribute " ).get();

		assertThat( cut.matches( div, "mySpecialAttribute" ) ).isTrue();
	}
}
