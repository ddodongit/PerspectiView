import { Card } from "@/components/ui/card";

import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import TestPage from "@/pages/TestPage";
import { MessageCircleMore } from "lucide-react";
import Comment from "./Comment";

export default function RefContents() {
  return (
    <Tabs
      defaultValue="relation"
      className="flex flex-col justify-between w-1/2 h-full m-5"
    >
      <TabsList className="flex items-center justify-start w-full ">
        <TabsTrigger value="relation" className="text-bold">
          전체 인물 관계도
        </TabsTrigger>
        <TabsTrigger value="foreshadowing" className="text-bold">
          복선 목록
        </TabsTrigger>
        <TabsTrigger value="comment" className="text-bold">
          <div className="flex ">
            <div className="mx-2">댓글</div>
            <div>
              <MessageCircleMore size={18} />
            </div>
          </div>
        </TabsTrigger>
      </TabsList>
      {/* 전체 인물 관계도 */}
      <TabsContent value="relation" className="h-full">
        <Card className="h-full "></Card>
      </TabsContent>

      {/* 복선 목록 */}
      <TabsContent value="foreshadowing" className="h-full">
        <TestPage teamId={7} productId={1} />
        {/* <ForeshadowingTab /> */}
      </TabsContent>

      {/* 의견 */}
      <TabsContent value="comment" className="h-full">
        <Card className="h-full ">
          <Comment />
        </Card>
      </TabsContent>
    </Tabs>
  );
}
