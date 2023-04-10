import React, { useEffect, useState } from "react";
import { useLocation } from "react-router";
import { useCookies } from "react-cookie";
import ProductCard from "./ProductCard";

function ProductsTable({ customers }) {
  const headerRow = [
    <tr key="hd">
      <th key="1"> ID# </th>
      <th key="2"> Name </th>
      <th key="3"> Sku </th>
      <th key="4"> Category </th>
      <th key="5"> Price </th>
      <th key="6"> Discount% </th>
      <th key="7"> Created-At </th>
      <th key="8"> Modified_At </th>
    </tr>,
  ];

  let tableRows = [];
  let contents = Object.values(products.data.content);

  let count = 0;

  for (var i = 0; i < contents.length; i++) {
    let emptyRowCount = false;
    tableRows[i] = (
      <tr key={"row" + count++ + i}>
        {Object.entries(contents[i]).map(([k, v]) =>
          k !== "links" && k !== "discountDTO" ? (
            <td key={"col" + count++}>{v}</td>
          ) : null
        )}
      </tr>
    );
  }

  return (
    <table className="table">
      <thead>{headerRow}</thead>
      <tbody>{tableRows}</tbody>
    </table>
  );
}

export default function Customers() {
  const { state } = useLocation();
  const [content, setContent] = useState([]);
  useEffect(() => {}, [content]);

  let customers = {
    data: {
      links: [
        {
          rel: "self",
          href: "http://localhost:8080/products?pageNo=0&pageSize=2&sortBy=name%2Csku&sortDirection=ASC",
        },
      ],
      content: [
        {
          id: 5,
          name: "Apple 2022 MacBook Pro Laptop",
          description:
            "Apple 2022 MacBook Pro Laptop with M2 chip: 13-inch Retina Display, 8GB RAM, 256GB Storage, Touch Bar, Backlit Keyboard, FaceTime HD Camera. Works with iPhone and iPad; Space Gray",
          sku: "if364428",
          category: "Devices",
          price: 1149.0,
          discountDTO: null,
          createdAt: "2023-03-18T13:09:28.956842",
          modifiedAt: "2023-03-18T13:09:28.956842",
          links: [
            {
              rel: "self",
              href: "http://localhost:8080/product/5",
            },
          ],
        },
        {
          id: 6,
          name: "Rich Dad Poor Dad",
          description:
            " What the Rich Teach Their Kids About Money That the Poor and Middle Class Do Not! ",
          sku: "ti910002",
          category: "Book",
          price: 12.99,
          discountDTO: null,
          createdAt: "2023-03-18T13:09:28.957843",
          modifiedAt: "2023-03-18T13:09:28.957843",
          links: [
            {
              rel: "self",
              href: "http://localhost:8080/product/6",
            },
          ],
        },
      ],
    },
    next_page_url: {
      rel: "self",
      href: "http://localhost:8080/products?pageNo=1&pageSize=2&sortBy=name%2Csku&sortDirection=ASC",
    },
    "other-info": {
      sort: {
        empty: false,
        sorted: true,
        unsorted: false,
      },
      offset: 0,
      pageNumber: 0,
      pageSize: 2,
      paged: true,
      unpaged: false,
    },
  };

  function Screen() {
    return (
      <div className="customers-table-container">
        {<CustomersTable customers={customers} />}
      </div>
    );
  }

  return <Screen />;
}
