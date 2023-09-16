import React, { Component, useEffect } from "react";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import "./eventBooking.css";
import { useState } from "react";
const EventBooking = () => {
  //   let countMap = [{ count: 2 }, { count: 2 }, { count: 2 }];
  const [counts, setCounts] = useState([{ id: 0, count: 0 }]);
  //   const [count0, setCount0] = useState(0);
  //   const [count1, setCount1] = useState(0);
  //   const [count2, setCount2] = useState(0);
  function createData(name, currency, capacity, remaining, price) {
    return { name, currency, capacity, remaining, price };
  }

  const rows = [
    createData("VIP", "AUD", 30, 25, 100),
    createData("Normal", "AUD", 30, 10, 75),
    createData("Standing", "AUD", 30, 30, 50),
  ];
  useEffect(() => {
    const newCounts = rows.map((row, index) => ({
      id: index,
      count: 0,
      price: row.price,
    }));
    setCounts(newCounts);
  }, []);
  const increment = (index) => {
    setCounts((prevCounts) =>
      prevCounts.map((counter) =>
        counter.id === index
          ? { ...counter, count: counter.count + 1 }
          : counter
      )
    );
  };
  const decrement = (index) => {
    setCounts((prevCounts) =>
      prevCounts.map((counter) =>
        counter.id === index && counter.count > 0
          ? { ...counter, count: counter.count - 1 }
          : counter
      )
    );
  };
  return (
    <>
      <h1>it is booking system</h1>
      <TableContainer component={Paper}>
        <Table sx={{ minWidth: 650 }} aria-label="simple table">
          <TableHead>
            <TableRow>
              <TableCell sx={{ fontWeight: "bold" }}>Section Name</TableCell>
              <TableCell align="center" sx={{ fontWeight: "bold" }}>
                Currency
              </TableCell>
              <TableCell align="center" sx={{ fontWeight: "bold" }}>
                Ticket Capacity
              </TableCell>
              <TableCell align="center" sx={{ fontWeight: "bold" }}>
                Ticket Remaining
              </TableCell>
              {/* <TableCell align="center">Price</TableCell> */}
            </TableRow>
          </TableHead>
          <TableBody>
            {rows.map((row, index) => (
              <TableRow
                key={row.name}
                sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
              >
                <TableCell component="th" scope="row">
                  {row.name}
                  <br />
                  {row.price}$ for each
                  {/* <div class="counter">
                    <button onclick={() => decrement(index)}>-</button>
                    <span>{counts[index].counter}</span>
                    <button onclick={() => increment(index)}>+</button>
                  </div> */}
                  {counts.length !== rows.length ? (
                    <></>
                  ) : (
                    <div class="counter">
                      <button onClick={() => decrement(index)}>-</button>
                      <span>
                        {counts[index].count}
                        {/* {index} */}
                      </span>
                      <button onClick={() => increment(index)}>+</button>
                    </div>
                  )}
                </TableCell>
                <TableCell align="center">{row.currency}</TableCell>
                <TableCell align="center">{row.capacity}</TableCell>
                <TableCell align="center">{row.remaining}</TableCell>
                {/* <TableCell align="center">{row.price}</TableCell> */}
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
      {/* <h3>
        Total fee:{" "}
        {counts.reduce(
          (accumulator, counter) => accumulator + counter.count * counter.price,
          0
        )}
      </h3> */}
    </>
  );
};

export default EventBooking;
// class EventBooking extends React.Component {
//   constructor() {
//     super();
//     this.state = {
//       seat: [
//         "A1",
//         "A2",
//         "A3",
//         "A4",
//         "A5",
//         "A6",
//         "A7",
//         "B1",
//         "B2",
//         "B3",
//         "B4",
//         "B5",
//         "B6",
//         "B7",
//         "C1",
//         "C2",
//         "C3",
//         "C4",
//         "C5",
//         "C6",
//         "C7",
//       ],
//       seatRows: ["A", "B", "C"],
//       seatAvailable: [
//         "A1",
//         "A2",
//         "A3",
//         "A4",
//         "A5",
//         "A6",
//         "A7",
//         "B1",
//         "B2",
//         "B3",
//         "B4",
//         "B5",
//         "B6",
//         "B7",
//         "C1",
//         "C2",
//         "C3",
//         "C4",
//         "C5",
//         "C6",
//         "C7",
//       ],
//       seatReserved: [],
//       seatSelected: ["A7", "B7", "C7"],
//     };
//   }

//   onClickData(seat) {
//     if (this.state.seatReserved.indexOf(seat) > -1) {
//       this.setState({
//         seatAvailable: this.state.seatAvailable.concat(seat),
//         seatReserved: this.state.seatReserved.filter((res) => res !== seat),
//         //seatSelected: this.state.seatSelected.filter(res => res != seat)
//       });
//     } else {
//       this.setState({
//         seatReserved: this.state.seatReserved.concat(seat),
//         //seatSelected: this.state.seatSelected.concat(seat),
//         seatAvailable: this.state.seatAvailable.filter((res) => res !== seat),
//       });
//     }
//   }
//   checktrue(row) {
//     if (this.state.seatSelected.indexOf(row) > -1) {
//       return false;
//     } else {
//       return true;
//     }
//   }

//   handleSubmited() {
//     console.log("U CLICK ME ");
//     this.setState({
//       seatSelected: this.state.seatSelected.concat(this.state.seatReserved),
//     });
//     this.setState({
//       seatReserved: [],
//     });
//   }

//   render() {
//     return (
//       <div>
//         <h1>Seat Booking System</h1>
//         <DrawGrid
//           seat={this.state.seat}
//           seatRows={this.state.seatRows}
//           available={this.state.seatAvailable}
//           reserved={this.state.seatReserved}
//           selected={this.state.seatSelected}
//           onClickData={this.onClickData.bind(this)}
//           checktrue={this.checktrue.bind(this)}
//           handleSubmited={this.handleSubmited.bind(this)}
//         />
//       </div>
//     );
//   }
// }

// class DrawGrid extends React.Component {
//   render() {
//     return (
//       <div className="container">
//         <table className="grid">
//           <tbody>
//             {this.props.seatRows.map((row) => {
//               return (
//                 <tr>
//                   {this.props.seat
//                     .filter((seat) => seat.startsWith(row))
//                     .map((s) => {
//                       return (
//                         <td
//                           className={
//                             this.props.selected.indexOf(s) > -1
//                               ? "reserved"
//                               : this.props.reserved.indexOf(s) > -1
//                               ? "selected"
//                               : "available"
//                           }
//                           key={s}
//                           onClick={
//                             this.props.checktrue(s)
//                               ? (e) => this.onClickSeat(s)
//                               : null
//                           }
//                         >
//                           {s}{" "}
//                         </td>
//                       );
//                     })}
//                 </tr>
//               );
//             })}
//           </tbody>
//         </table>
//         <button
//           type="button"
//           //   className="button"
//           onClick={() => this.props.handleSubmited()}
//         >
//           Confirm Booking
//         </button>
//       </div>
//     );
//   }

//   onClickSeat(seat) {
//     this.props.onClickData(seat);
//   }
// }
// export default EventBooking;
