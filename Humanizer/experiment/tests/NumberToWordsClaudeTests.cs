namespace Humanizer.Tests;

/// <summary>
/// Claude-generated tests for NumberToWordsExtension.
/// Covers: ToWords (int/long), ToOrdinalWords, ToTuple — all in en-US locale.
/// Deterministic: no system-time dependency; explicit en-US culture throughout.
/// </summary>
[UseCulture("en-US")]
public class NumberToWordsClaudeTests
{
    // ── ToWords(int) — cardinal numbers ──────────────────────────────────────

    [Theory]
    [InlineData(0,    "zero")]
    [InlineData(1,    "one")]
    [InlineData(2,    "two")]
    [InlineData(3,    "three")]
    [InlineData(4,    "four")]
    [InlineData(5,    "five")]
    [InlineData(10,   "ten")]
    [InlineData(11,   "eleven")]
    [InlineData(12,   "twelve")]
    [InlineData(13,   "thirteen")]
    [InlineData(15,   "fifteen")]
    [InlineData(19,   "nineteen")]
    [InlineData(20,   "twenty")]
    [InlineData(21,   "twenty-one")]
    [InlineData(30,   "thirty")]
    [InlineData(40,   "forty")]
    [InlineData(99,   "ninety-nine")]
    [InlineData(100,  "one hundred")]
    [InlineData(101,  "one hundred and one")]
    [InlineData(999,  "nine hundred and ninety-nine")]
    [InlineData(1000, "one thousand")]
    public void ToWords_SmallIntegers_ReturnsExpectedWords(int number, string expected)
    {
        var result = number.ToWords();
        Assert.Equal(expected, result);
        Assert.NotEmpty(result);
    }

    [Fact]
    public void ToWords_Zero_ReturnsZero()
    {
        Assert.Equal("zero", 0.ToWords());
    }

    [Fact]
    public void ToWords_One_ReturnsOne()
    {
        var result = 1.ToWords();
        Assert.Equal("one", result);
        Assert.DoesNotContain(" ", result);
    }

    [Fact]
    public void ToWords_NegativeOne_ContainsMinusOrMinus()
    {
        var result = (-1).ToWords();
        Assert.NotEmpty(result);
        Assert.Contains("minus", result, StringComparison.OrdinalIgnoreCase);
    }

    [Fact]
    public void ToWords_NegativeHundred_ContainsMinusAndHundred()
    {
        var result = (-100).ToWords();
        Assert.Contains("minus", result, StringComparison.OrdinalIgnoreCase);
        Assert.Contains("hundred", result, StringComparison.OrdinalIgnoreCase);
    }

    [Fact]
    public void ToWords_LargeNumber_ContainsThousand()
    {
        var result = 1_000_000.ToWords();
        Assert.NotEmpty(result);
        Assert.Contains("million", result, StringComparison.OrdinalIgnoreCase);
    }

    [Fact]
    public void ToWords_OneBillion_ReturnsBillion()
    {
        var result = 1_000_000_000.ToWords();
        Assert.Contains("billion", result, StringComparison.OrdinalIgnoreCase);
    }

    // ── ToWords(long) ────────────────────────────────────────────────────────

    [Fact]
    public void ToWords_LongZero_ReturnsZero()
    {
        Assert.Equal("zero", 0L.ToWords());
    }

    [Fact]
    public void ToWords_LongOneMillion_ReturnsMillion()
    {
        var result = 1_000_000L.ToWords();
        Assert.Contains("million", result, StringComparison.OrdinalIgnoreCase);
    }

    // ── ToWords — explicit culture ────────────────────────────────────────────

    [Fact]
    public void ToWords_ExplicitEnUsCulture_SameAsImplicit()
    {
        var culture = new CultureInfo("en-US");
        Assert.Equal(5.ToWords(), 5.ToWords(culture));
        Assert.Equal(42.ToWords(), 42.ToWords(culture));
    }

    // ── ToOrdinalWords(int) ──────────────────────────────────────────────────

    [Theory]
    [InlineData(0,   "zeroth")]
    [InlineData(1,   "first")]
    [InlineData(2,   "second")]
    [InlineData(3,   "third")]
    [InlineData(4,   "fourth")]
    [InlineData(5,   "fifth")]
    [InlineData(6,   "sixth")]
    [InlineData(7,   "seventh")]
    [InlineData(8,   "eighth")]
    [InlineData(9,   "ninth")]
    [InlineData(10,  "tenth")]
    [InlineData(11,  "eleventh")]
    [InlineData(12,  "twelfth")]
    [InlineData(20,  "twentieth")]
    [InlineData(21,  "twenty-first")]
    [InlineData(100, "hundredth")]
    [InlineData(1000,"thousandth")]
    public void ToOrdinalWords_CommonValues_ReturnsExpectedOrdinal(int number, string expected)
    {
        var result = number.ToOrdinalWords();
        Assert.Equal(expected, result);
        Assert.NotEmpty(result);
    }

    [Fact]
    public void ToOrdinalWords_First_IsFirst()
    {
        var result = 1.ToOrdinalWords();
        Assert.Equal("first", result);
    }

    [Fact]
    public void ToOrdinalWords_Second_IsSecond()
    {
        Assert.Equal("second", 2.ToOrdinalWords());
    }

    [Fact]
    public void ToOrdinalWords_Third_IsThird()
    {
        Assert.Equal("third", 3.ToOrdinalWords());
    }

    [Fact]
    public void ToOrdinalWords_ExplicitCulture_MatchesImplicit()
    {
        var culture = new CultureInfo("en-US");
        Assert.Equal(4.ToOrdinalWords(), 4.ToOrdinalWords(culture));
        Assert.Equal(10.ToOrdinalWords(), 10.ToOrdinalWords(culture));
    }

    // ── ToOrdinalWords — boundary values ─────────────────────────────────────

    [Theory]
    [InlineData(30,   "thirtieth")]
    [InlineData(40,   "fortieth")]
    [InlineData(50,   "fiftieth")]
    [InlineData(60,   "sixtieth")]
    [InlineData(70,   "seventieth")]
    [InlineData(80,   "eightieth")]
    [InlineData(90,   "ninetieth")]
    [InlineData(95,   "ninety-fifth")]
    [InlineData(100,  "hundredth")]
    [InlineData(112,  "hundred and twelfth")]
    [InlineData(1000, "thousandth")]
    public void ToOrdinalWords_RoundAndBoundaryValues_AreCorrect(int number, string expected) =>
        Assert.Equal(expected, number.ToOrdinalWords());

    // ── ToTuple ───────────────────────────────────────────────────────────────

    [Theory]
    [InlineData(1,  "single")]
    [InlineData(2,  "double")]
    [InlineData(3,  "triple")]
    [InlineData(4,  "quadruple")]
    [InlineData(5,  "quintuple")]
    [InlineData(6,  "sextuple")]
    [InlineData(7,  "septuple")]
    [InlineData(8,  "octuple")]
    [InlineData(9,  "nonuple")]
    [InlineData(10, "decuple")]
    [InlineData(100,"centuple")]
    public void ToTuple_CommonValues_ReturnsCorrectTupleName(int number, string expected)
    {
        var result = number.ToTuple();
        Assert.Equal(expected, result);
        Assert.NotEmpty(result);
        Assert.True(result.All(char.IsLetter));
    }

    [Fact]
    public void ToTuple_ExplicitCulture_MatchesImplicit()
    {
        var culture = new CultureInfo("en-US");
        Assert.Equal(1.ToTuple(), 1.ToTuple(culture));
        Assert.Equal(3.ToTuple(), 3.ToTuple(culture));
    }

    // ── Output structural invariants ─────────────────────────────────────────

    [Theory]
    [InlineData(1)]
    [InlineData(15)]
    [InlineData(100)]
    [InlineData(1000)]
    public void ToWords_ResultIsNonEmpty(int number)
    {
        Assert.NotEmpty(number.ToWords());
    }

    [Theory]
    [InlineData(1)]
    [InlineData(10)]
    [InlineData(100)]
    public void ToOrdinalWords_ResultIsNonEmpty(int number)
    {
        Assert.NotEmpty(number.ToOrdinalWords());
    }

    [Fact]
    public void ToWords_ResultIsLowerCase_ForPositiveNumbers()
    {
        foreach (var n in new[] { 1, 5, 10, 20, 100, 1000 })
        {
            var w = n.ToWords();
            Assert.Equal(w, w.ToLower(), StringComparer.Ordinal);
        }
    }
}
