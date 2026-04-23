namespace Humanizer.Tests;

/// <summary>
/// Claude-generated tests for StringHumanizeExtensions.
/// Covers: PascalCase, camelCase, underscore/dash separation, acronyms, casing overloads, and edge cases.
/// </summary>
[UseCulture("en-US")]
public class StringHumanizeClaudeTests
{
    // ── Humanize() — PascalCase ──────────────────────────────────────────────

    [Fact]
    public void Humanize_SingleLowercaseLetter_CapitalizesIt()
    {
        var result = "a".Humanize();
        Assert.Equal("A", result);
        Assert.Equal(1, result.Length);
    }

    [Fact]
    public void Humanize_SingleUppercaseLetter_ReturnsSame()
    {
        var result = "A".Humanize();
        Assert.Equal("A", result);
    }

    [Fact]
    public void Humanize_EmptyString_ReturnsEmpty()
    {
        var result = "".Humanize();
        Assert.Equal("", result);
        Assert.Equal(0, result.Length);
    }

    [Fact]
    public void Humanize_SimpleWord_CapitalizesFirstLetter()
    {
        var result = "car".Humanize();
        Assert.Equal("Car", result);
        Assert.True(char.IsUpper(result[0]));
    }

    [Theory]
    [InlineData("PascalCaseInputStringIsTurnedIntoSentence",
                "Pascal case input string is turned into sentence")]
    [InlineData("WhenIUseAnInputAHere", "When I use an input a here")]
    [InlineData("camelCaseText", "Camel case text")]
    [InlineData("XIsFirstWordInTheSentence", "X is first word in the sentence")]
    public void Humanize_PascalAndCamelCase_ProducesSpacedSentence(string input, string expected)
    {
        var result = input.Humanize();
        Assert.Equal(expected, result);
        Assert.True(char.IsUpper(result[0]));
    }

    [Fact]
    public void Humanize_PascalCase_FirstCharAlwaysUpper()
    {
        var result = "someValue".Humanize();
        Assert.True(char.IsUpper(result[0]));
        Assert.Contains(" ", result);
    }

    // ── Humanize() — Acronyms ────────────────────────────────────────────────

    [Fact]
    public void Humanize_AllCapsAcronym_ReturnsUnchanged()
    {
        Assert.Equal("HTML", "HTML".Humanize());
        Assert.Equal("XML", "XML".Humanize());
        Assert.Equal("API", "API".Humanize());
    }

    [Theory]
    [InlineData("TheHTMLLanguage", "The HTML language")]
    [InlineData("HTMLIsTheLanguage", "HTML is the language")]
    [InlineData("TheLanguageIsHTML", "The language is HTML")]
    [InlineData("HTML5", "HTML 5")]
    [InlineData("1HTML", "1 HTML")]
    public void Humanize_EmbeddedAcronym_PreservesAcronymCase(string input, string expected)
    {
        var result = input.Humanize();
        Assert.Equal(expected, result);
    }

    // ── Humanize() — Underscore / dash separation ────────────────────────────

    [Theory]
    [InlineData("underscore_separated", "Underscore separated")]
    [InlineData("dash-separated-string", "Dash separated string")]
    [InlineData("Underscored_input_string_is_turned_into_sentence",
                "Underscored input string is turned into sentence")]
    [InlineData("Underscored_input_String_is_turned_INTO_sentence",
                "Underscored input String is turned INTO sentence")]
    public void Humanize_UnderscoreOrDashSeparated_ProducesSpacedString(string input, string expected)
    {
        var result = input.Humanize();
        Assert.Equal(expected, result);
        Assert.DoesNotContain("_", result);
        Assert.DoesNotContain("-", result);
    }

    [Theory]
    [InlineData("TEST 1 - THIS IS A TEST", "TEST 1 THIS IS A TEST")]
    [InlineData("TEST 1 _ THIS IS A TEST", "TEST 1 THIS IS A TEST")]
    [InlineData("TEST 1 - THIS_IS_A_TEST", "TEST 1 THIS IS A TEST")]
    public void Humanize_FreestandingDashOrUnderscore_RemovesSeparator(string input, string expected) =>
        Assert.Equal(expected, input.Humanize());

    // ── Humanize() — Special characters / Unicode ───────────────────────────

    [Fact]
    public void Humanize_OnlySpecialChars_ReturnsEmpty()
    {
        Assert.Equal("", "?)@".Humanize());
        Assert.Equal("", "?".Humanize());
    }

    [Fact]
    public void Humanize_ContainsSpecialChars_FiltersNonLetterNonDigit()
    {
        var result = "ContainsSpecial?)@Characters".Humanize();
        Assert.Equal("Contains special characters", result);
    }

    [Fact]
    public void Humanize_FrenchAccentedChars_Preserved()
    {
        var result = "JeNeParlePasFrançais".Humanize();
        Assert.Equal("Je ne parle pas français", result);
    }

    [Fact]
    public void Humanize_NumericWithWord_SeparatesWithSpace()
    {
        var result = "10IsInTheBegining".Humanize();
        Assert.Equal("10 is in the begining", result);
        Assert.StartsWith("10", result);
    }

    // ── Humanize(LetterCasing) overload ──────────────────────────────────────

    [Theory]
    [InlineData("PascalCaseInputString", LetterCasing.AllCaps, "PASCAL CASE INPUT STRING")]
    [InlineData("PascalCaseInputString", LetterCasing.LowerCase, "pascal case input string")]
    [InlineData("PascalCaseInputString", LetterCasing.Sentence, "Pascal case input string")]
    [InlineData("PascalCaseInputString", LetterCasing.Title, "Pascal Case Input String")]
    public void Humanize_WithCasing_AppliesCasingCorrectly(
        string input, LetterCasing casing, string expected)
    {
        var result = input.Humanize(casing);
        Assert.Equal(expected, result);
    }

    [Fact]
    public void Humanize_AllCapsWithLowerCaseCasing_ProducesLowerCase()
    {
        var result = "LOWERCASE".Humanize(LetterCasing.LowerCase);
        Assert.Equal("lowercase", result);
        Assert.True(result.All(c => !char.IsUpper(c)));
    }

    [Fact]
    public void Humanize_UnderscoreWithAllCaps_ProducesUpperCase()
    {
        var result = "Can_Humanize_into_Upper_case".Humanize(LetterCasing.AllCaps);
        Assert.Equal("CAN HUMANIZE INTO UPPER CASE", result);
        Assert.True(result.Replace(" ", "").All(char.IsUpper));
    }

    [Fact]
    public void Humanize_TitleCasingPreservesAcronyms()
    {
        var result = "Title_humanization_Honors_ALLCAPS".Humanize(LetterCasing.Title);
        Assert.Equal("Title Humanization Honors ALLCAPS", result);
        Assert.Contains("ALLCAPS", result);
    }

    // ── Output structural invariants ─────────────────────────────────────────

    [Theory]
    [InlineData("SomeValue")]
    [InlineData("some_value")]
    [InlineData("some-value")]
    public void Humanize_NonEmpty_ResultStartsWithUpper(string input)
    {
        var result = input.Humanize();
        Assert.NotEmpty(result);
        Assert.True(char.IsUpper(result[0]));
    }

    [Theory]
    [InlineData("TwoWords")]
    [InlineData("three_word_string")]
    [InlineData("multi-word-dash")]
    public void Humanize_MultiWord_ContainsSpaces(string input)
    {
        var result = input.Humanize();
        Assert.Contains(" ", result);
    }
}