package de.retest.recheck.review.ignore.matcher;

import java.util.Optional;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import de.retest.recheck.review.ignore.io.RegexLoader;
import de.retest.recheck.ui.Path;
import de.retest.recheck.ui.descriptors.Element;

public class ElementXPathMatcher implements Matcher<Element> {

	private final String givenXPath;
	private final String normalizedXPath;

	public ElementXPathMatcher( final Element element ) {
		// do not normalize again
		normalizedXPath = element.getIdentifyingAttributes().getPath();
		givenXPath = normalizedXPath;
	}

	private ElementXPathMatcher( final String xpath ) {
		givenXPath = xpath;
		// normalize xpath
		normalizedXPath = Path.fromString( xpath ).toString();
	}

	@Override
	public boolean test( final Element element ) {
		return element.getIdentifyingAttributes().getPath().equals( normalizedXPath );
	}

	@Override
	public String toString() {
		return String.format( ElementXpathMatcherLoader.FORMAT, givenXPath );
	}

	public static final class ElementXpathMatcherLoader extends RegexLoader<ElementXPathMatcher> {

		private static final String XPATH = "xpath=";

		private static final String FORMAT = XPATH + "%s";
		private static final Pattern REGEX = Pattern.compile( XPATH + "(.+)" );

		public ElementXpathMatcherLoader() {
			super( REGEX );
		}

		@Override
		protected Optional<ElementXPathMatcher> load( final MatchResult matcher ) {
			final String xpath = matcher.group( 1 );
			return Optional.of( new ElementXPathMatcher( xpath ) );
		}
	}
}
