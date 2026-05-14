using Humanizer;
using Xunit;

namespace Humanizer.Tests;

public class StringHumanizeGptTests
{
    [Fact]
    public void Humanize_Should_Handle_PascalCase()
    {
        // Arrange
        var input = "PascalCaseInputString";

        // Act
        var result = input.Humanize();

        // Assert
        Assert.Equal("Pascal case input string", result);
        Assert.StartsWith("Pascal", result);
        Assert.DoesNotContain("_", result);
    }

    [Fact]
    public void Humanize_Should_Handle_CamelCase()
    {
        // Arrange
        var input = "camelCaseText";

        // Act
        var result = input.Humanize();

        // Assert
        Assert.Equal("Camel case text", result);
        Assert.Contains("case", result);
    }

    [Fact]
    public void Humanize_Should_Handle_Underscores()
    {
        // Arrange
        var input = "underscored_input_string";

        // Act
        var result = input.Humanize();

        // Assert
        Assert.Equal("underscored input string", result);
        Assert.DoesNotContain("_", result);
        Assert.Contains("input", result);
    }

    [Fact]
    public void Humanize_Should_Handle_Dashes()
    {
        // Arrange
        var input = "dash-separated-string";

        // Act
        var result = input.Humanize();

        // Assert
        Assert.Equal("dash separated string", result);
        Assert.DoesNotContain("-", result);
    }

    [Fact]
    public void Humanize_Should_Preserve_AllCaps_Acronym()
    {
        // Arrange
        var input = "HTML";

        // Act
        var result = input.Humanize();

        // Assert
        Assert.Equal("HTML", result);
        Assert.All(result, c => Assert.True(char.IsUpper(c)));
    }

    [Fact]
    public void Humanize_Should_Handle_Freestanding_Underscores()
    {
        // Arrange
        var input = "some _ value";

        // Act
        var result = input.Humanize();

        // Assert
        Assert.Equal("Some value", result);
        Assert.DoesNotContain("_", result);
    }

    [Fact]
    public void Humanize_Should_Handle_Freestanding_Dashes()
    {
        // Arrange
        var input = "some - value";

        // Act
        var result = input.Humanize();

        // Assert
        Assert.Equal("Some value", result);
        Assert.DoesNotContain("-", result);
    }

    [Fact]
    public void Humanize_Should_Handle_Empty_String()
    {
        // Arrange
        var input = string.Empty;

        // Act
        var result = input.Humanize();

        // Assert
        Assert.Equal(string.Empty, result);
        Assert.Empty(result);
    }

    [Fact]
    public void Humanize_With_LowerCase_Should_Apply_Casing()
    {
        // Arrange
        var input = "PascalCaseInput";

        // Act
        var result = input.Humanize(LetterCasing.LowerCase);

        // Assert
        Assert.Equal("pascal case input", result);
        Assert.DoesNotContain("Pascal", result);
    }

    [Fact]
    public void Humanize_With_TitleCase_Should_Apply_Casing()
    {
        // Arrange
        var input = "pascalCaseInput";

        // Act
        var result = input.Humanize(LetterCasing.Title);

        // Assert
        Assert.Equal("Pascal Case Input", result);
        Assert.StartsWith("Pascal", result);
    }
}