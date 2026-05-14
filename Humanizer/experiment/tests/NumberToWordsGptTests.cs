using System.Globalization;
using Humanizer;
using Xunit;

namespace Humanizer.Tests;

public class NumberToWordsGptTests
{
    [Fact]
    public void ToWords_Should_Convert_Simple_Number()
    {
        // Arrange
        var number = 5;

        // Act
        var result = number.ToWords(new CultureInfo("en-US"));

        // Assert
        Assert.Equal("five", result);
        Assert.Contains("five", result);
    }

    [Fact]
    public void ToWords_Should_Convert_Compound_Number()
    {
        // Arrange
        var number = 42;

        // Act
        var result = number.ToWords(new CultureInfo("en-US"));

        // Assert
        Assert.Contains("forty", result);
        Assert.Contains("two", result);
    }

    [Fact]
    public void ToWords_Should_Handle_Zero()
    {
        // Arrange
        var number = 0;

        // Act
        var result = number.ToWords(new CultureInfo("en-US"));

        // Assert
        Assert.Equal("zero", result);
        Assert.NotEmpty(result);
    }

    [Fact]
    public void ToWords_Should_Handle_Negative_Number()
    {
        // Arrange
        var number = -5;

        // Act
        var result = number.ToWords(new CultureInfo("en-US"));

        // Assert
        Assert.Contains("minus", result);
        Assert.Contains("five", result);
    }

    [Fact]
    public void ToOrdinalWords_Should_Convert_First()
    {
        // Arrange
        var number = 1;

        // Act
        var result = number.ToOrdinalWords(new CultureInfo("en-US"));

        // Assert
        Assert.Equal("first", result);
        Assert.StartsWith("first", result);
    }

    [Fact]
    public void ToOrdinalWords_Should_Convert_Regular_Ordinal()
    {
        // Arrange
        var number = 5;

        // Act
        var result = number.ToOrdinalWords(new CultureInfo("en-US"));

        // Assert
        Assert.Contains("fifth", result);
    }

    [Fact]
    public void ToOrdinalWords_Should_Use_Native_Slovak_First_Ordinal()
    {
        // Arrange
        var number = 1;

        // Act
        var result = number.ToOrdinalWords(
            GrammaticalGender.Masculine,
            new CultureInfo("sk-SK"));

        // Assert
        Assert.Equal("prvý", result);
        Assert.DoesNotContain("one", result);
    }

    [Fact]
    public void ToTuple_Should_Convert_Single()
    {
        // Arrange
        var number = 1;

        // Act
        var result = number.ToTuple(new CultureInfo("en-US"));

        // Assert
        Assert.Equal("single", result);
    }

    [Fact]
    public void ToWords_With_AddAnd_Should_Include_And()
    {
        // Arrange
        var number = 101L;

        // Act
        var result = number.ToWords(
            new CultureInfo("en-GB"),
            true);

        // Assert
        Assert.Contains("and", result);
    }

    [Fact]
    public void ToWords_With_AddAnd_False_Should_Not_Force_And()
    {
        // Arrange
        var number = 101L;

        // Act
        var result = number.ToWords(
            new CultureInfo("en-US"),
            false);

        // Assert
        Assert.Contains("one", result);
        Assert.NotEmpty(result);
    }
}