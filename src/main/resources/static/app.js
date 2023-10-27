let playerId;
let opponentId;
const baseUrl = "http://localhost:8080/v1/games/";
const stompClient = new StompJs.Client({
    brokerURL: "ws://localhost:8080/stomp"
});


stompClient.onConnect = () => {
    setConnected(true);
    stompClient.subscribe("/topic/games/" + $("#game").val(), (response) => {
        const game = JSON.parse(response.body);
        updateHistory(game);
        updateState(game);
    });
};

function create() {
    $.ajax({
        url: baseUrl,
        type: "POST",
        success: (game) => {
            $("#game").val(game.id)
            $("#info").text("The game was created. Send game ID to your friend for them to connect.");
            stompClient.activate();
            playerId = 0;
            opponentId = 1;
        }
    })
}

function connect() {
    $.ajax({
        url: baseUrl + $("#game").val() + "/players",
        type: "POST",
        success: () => {
            $("#info").text("The game has started. Good luck! Waiting for the opponent’s turn.");
            stompClient.activate();
            playerId = 1;
            opponentId = 0;
        }
    })
}

function disconnect() {
    $.ajax({
        url: baseUrl + $("#game").val() + "/players/" + playerId,
        type: "DELETE",
        success: () => {
            $("#info").text("Create a new game or connect to an existing one using game ID.");
            $("#game").val("");
            stompClient.deactivate();
            setConnected(false);
        }
    })
}

function makeTurn(pit) {
    $.ajax({
        url: baseUrl + $("#game").val() + "/turns",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({row: playerId, col: pit}),
    });
}

function updateHistory(game) {
    $("#history-entries").prepend(
        "<tr>" +
        "<td>" + game.status + "</td>" +
        "<td>" + game.players.active + "</td>" +
        "<td>" + game.board.pits + "</td>" +
        "</tr>");
}

function updateState(game) {
    const isMyTurn = game.players.active === playerId;
    updatePits(game, isMyTurn);

    const isGameOver = game.result.winner !== -1;
    if (isGameOver) {
        setWinnerMessage(game);
    } else {
        setTurnMessage(isMyTurn);
    }
}

function updatePits(game, isMyTurn) {
    for (let i = 0; i < 6; i++) {
        $("#pit-0" + i).prop("disabled", !isMyTurn).html(game.board.pits[playerId][i]);
        $("#pit-1" + i).html(game.board.pits[opponentId][i]);
    }

    $("#mancala-0").html(game.board.pits[playerId][6]);
    $("#mancala-1").html(game.board.pits[opponentId][6]);
}

function setWinnerMessage(game) {
    $("#info").text(playerId === game.result.winner
        ? "The game is over. You win. Congrats!"
        : "The game is over. You lose. Better luck next time!");
}

function setTurnMessage(isMyTurn) {
    $("#info").text(isMyTurn
        ? "Your turn."
        : "Waiting for the opponent’s turn.");
}

function setConnected(isConnected) {
    $("#connect").prop("disabled", isConnected);
    $("#create").prop("disabled", isConnected);
    $("#game").prop("disabled", isConnected);
    $("#disconnect").prop("disabled", !isConnected);
    $("#history").toggle();
    $("#history-entries").html("");
}

$(function () {
    $("form").on("submit", (e) => e.preventDefault());
    $("#connect").click(() => connect());
    $("#disconnect").click(() => disconnect());
    $("#create").click(() => create());

    $("#pit-00").click(() => makeTurn(0));
    $("#pit-01").click(() => makeTurn(1));
    $("#pit-02").click(() => makeTurn(2));
    $("#pit-03").click(() => makeTurn(3));
    $("#pit-04").click(() => makeTurn(4));
    $("#pit-05").click(() => makeTurn(5));
});