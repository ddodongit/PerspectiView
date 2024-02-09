import ContentText from "@/components/ReadMore";
import { useState, useEffect } from "react";
import { Badge } from "../ui/badge";
import ReadMore from "@/components/ReadMore";
// import useProductStore from "@/store/useProductStore";
// import useProductQueryModule from "@/hook/useProductQueryModule";

const genreListData = [
  {
    genreId: "1",
    genreName: "SF",
  },
  {
    genreId: "2",
    genreName: "액션",
  },
  {
    genreId: "3",
    genreName: "드라마",
  },
  {
    genreId: "4",
    genreName: "서스펜스",
  },
];

const categoryListData = [
  {
    categoryId: "1",
    categoryName: "웹툰",
  },
  {
    categoryId: "2",
    categoryName: "시나리오",
  },
  {
    categoryId: "3",
    categoryName: "순문학",
  },
  {
    categoryId: "4",
    categoryName: "만화",
  },
  {
    categoryId: "5",
    categoryName: "에세이",
  },
  {
    categoryId: "6",
    categoryName: "웹소설",
  },
  {
    categoryId: "7",
    categoryName: "기타",
  },
];

const productSample = {
  productId: "1",
  productname: "싸피 생활",
  categoryId: "5",
  genreList: ["3", "4"],
  productInfo:
    "허리가 망가진다는 싸피 한 번 시작해봤습니다. 두근두근 열심히 해볼까?허리가 망가진다는 싸피 한 번 시작해봤습니다. 두근두근 열심히 해볼까?허리가 망가진다는 싸피 한 번 시작해봤습니다. 두근두근 열심히 해볼까?허리가 망가진다는 싸피 한 번 시작해봤습니다. 두근두근 열심히 해볼까?허리가 망가진다는 싸피 한 번 시작해봤습니다. 두근두근 열심히 해볼까?허리가 망가진다는 싸피 한 번 시작해봤습니다. 두근두근 열심히 해볼까?허리가 망가진다는 싸피 한 번 시작해봤습니다. 두근두근 열심히 해볼까?허리가 망가진다는 싸피 한 번 시작해봤습니다. 두근두근 열심히 해볼까?허리가 망가진다는 싸피 한 번 시작해봤습니다. 두근두근 열심히 해볼까?허리가 망가진다는 싸피 한 번 시작해봤습니다. 두근두근 열심히 해볼까?허리가 망가진다는 싸피 한 번 시작해봤습니다. 두근두근 열심히 해볼까?허리가 망가진다는 싸피 한 번 시작해봤습니다. 두근두근 열심히 해볼까?허리가 망가진다는 싸피 한 번 시작해봤습니다. 두근두근 열심히 해볼까?",
  plotList: [
    {
      plotId: "11",
      plotName: "싸피 시작하다",
      plotcolor: "#000000",
      storyList: [
        {
          storyId: "111",
          plotId: "11",
          storyTitle: "주인공 등장",
          storyContent: "화이트 드래곤이 울부짖었다 크아아앙",
          characList: [
            {
              id: "juingong21",
            },
            {
              id: "whitedragon",
            },
          ],
          position_x: 0.0,
          position_y: 10.0,
        },
      ],
    },
    {
      plotId: "12",
      plotname: "싸피 마감하다",
      plotcolor: "#000000",
      storyList: [
        {
          storyId: "111",
          plotId: "11",
          storyTitle: "주인공 등장",
          storyContent: "화이트 드래곤이 울부짖었다 크아아앙",
          characList: [
            {
              id: "juingong21",
            },
            {
              id: "whitedragon",
            },
          ],
          position_x: 0.0,
          position_y: 10.0,
        },
      ],
    },
  ],
};

const getGenreName = (genreId) => {
  const result = genreListData.filter((genre) => genre.genreId === genreId);
  return result[0].genreName;
};

const getCategoryName = (categoryId) => {
  const result = categoryListData.filter((category) => category.categoryId === categoryId);
  return result[0].categoryName;
};

export default function ProductHeader() {
  // const { productData, getProductIsSuccess } = useProductQueryModule("3");
  const [productInfo, setProductInfo] = useState([]);
  // useEffect(() => {
  //   if (productData) {
  //     // console.log("getproduct", getProductIsSuccess, productData)
  //     setProductInfo(() => productData);
  //   }
  // });

  console.log("getInfo", productInfo);
  return (
    <>
      <header className="w-full border rounded shadow-md">
        <nav
          className="flex flex-col items-center justify-between p-3 mx-auto "
          aria-label="Global"
        >
          <div className="flex items-center justify-between w-full my-2 ">
            {/* 작품 */}
            <div className="flex ">
              <div className="px-6 text-sm font-semibold leading-6 text-gray-900 min-w-24">
                작품명
              </div>

              <div className="w-full">{productSample.productname}</div>
            </div>

            <div className="flex items-center justify-between ">
              {/* 장르 */}
              <div className="flex ">
                <div className="px-6 text-sm font-semibold leading-6 text-gray-900 min-w-20">
                  장르
                </div>
                <div className="flex items-center justify-between mx-auto space-x-2">
                  {productSample.genreList.map((genreId, key) => (
                    <Badge key={key} variant="destructive" radius="full">
                      {getGenreName(genreId)}
                    </Badge>
                  ))}
                </div>
              </div>

              {/* 분류 */}
              <div className="flex ">
                <div className="px-6 text-sm font-semibold leading-6 text-gray-900 min-w-20">
                  분류
                </div>
                <div className="flex items-center justify-between mx-auto space-x-2">
                  <Badge className="text-stone-100 bg-badge" radius="full">
                    {getCategoryName(productSample.categoryId)}
                  </Badge>
                </div>
              </div>
            </div>
          </div>
          <div className="flex items-center justify-between w-full my-2 ">
            {/* 설명 */}
            <div className="flex w-full">
              <div className="px-6 text-sm font-semibold leading-6 text-gray-900 min-w-24">
                설명
              </div>

              <div className="w-full ">
                <ReadMore>{productSample.productInfo}</ReadMore>
              </div>
            </div>
          </div>
        </nav>
      </header>
    </>
  );
}
