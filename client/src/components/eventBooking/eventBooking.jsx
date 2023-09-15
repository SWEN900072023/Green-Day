import React, { Component } from "react";
import "./eventBooking.css";

class EventBooking extends React.Component {
  constructor() {
    super();
    this.state = {
      seat: [
        "A1",
        "A2",
        "A3",
        "A4",
        "A5",
        "A6",
        "A7",
        "B1",
        "B2",
        "B3",
        "B4",
        "B5",
        "B6",
        "B7",
        "C1",
        "C2",
        "C3",
        "C4",
        "C5",
        "C6",
        "C7",
      ],
      seatRows: ["A", "B", "C"],
      seatAvailable: [
        "A1",
        "A2",
        "A3",
        "A4",
        "A5",
        "A6",
        "A7",
        "B1",
        "B2",
        "B3",
        "B4",
        "B5",
        "B6",
        "B7",
        "C1",
        "C2",
        "C3",
        "C4",
        "C5",
        "C6",
        "C7",
      ],
      seatReserved: [],
      seatSelected: ["A7", "B7", "C7"],
    };
  }

  onClickData(seat) {
    if (this.state.seatReserved.indexOf(seat) > -1) {
      this.setState({
        seatAvailable: this.state.seatAvailable.concat(seat),
        seatReserved: this.state.seatReserved.filter((res) => res !== seat),
        //seatSelected: this.state.seatSelected.filter(res => res != seat)
      });
    } else {
      this.setState({
        seatReserved: this.state.seatReserved.concat(seat),
        //seatSelected: this.state.seatSelected.concat(seat),
        seatAvailable: this.state.seatAvailable.filter((res) => res !== seat),
      });
    }
  }
  checktrue(row) {
    if (this.state.seatSelected.indexOf(row) > -1) {
      return false;
    } else {
      return true;
    }
  }

  handleSubmited() {
    console.log("U CLICK ME ");
    this.setState({
      seatSelected: this.state.seatSelected.concat(this.state.seatReserved),
    });
    this.setState({
      seatReserved: [],
    });
  }

  render() {
    return (
      <div>
        <h1>Seat Booking System</h1>
        <DrawGrid
          seat={this.state.seat}
          seatRows={this.state.seatRows}
          available={this.state.seatAvailable}
          reserved={this.state.seatReserved}
          selected={this.state.seatSelected}
          onClickData={this.onClickData.bind(this)}
          checktrue={this.checktrue.bind(this)}
          handleSubmited={this.handleSubmited.bind(this)}
        />
      </div>
    );
  }
}

class DrawGrid extends React.Component {
  render() {
    return (
      <div className="container">
        <table className="grid">
          <tbody>
            {this.props.seatRows.map((row) => {
              return (
                <tr>
                  {this.props.seat
                    .filter((seat) => seat.startsWith(row))
                    .map((s) => {
                      return (
                        <td
                          className={
                            this.props.selected.indexOf(s) > -1
                              ? "reserved"
                              : this.props.reserved.indexOf(s) > -1
                              ? "selected"
                              : "available"
                          }
                          key={s}
                          onClick={
                            this.props.checktrue(s)
                              ? (e) => this.onClickSeat(s)
                              : null
                          }
                        >
                          {s}{" "}
                        </td>
                      );
                    })}
                </tr>
              );
            })}
          </tbody>
        </table>
        <button
          type="button"
          //   className="button"
          onClick={() => this.props.handleSubmited()}
        >
          Confirm Booking
        </button>
      </div>
    );
  }

  onClickSeat(seat) {
    this.props.onClickData(seat);
  }
}
export default EventBooking;
