import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger,
} from "@/components/ui/alert-dialog";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { ScrollArea } from "@/components/ui/scroll-area";
import { Separator } from "@/components/ui/separator";
import { Textarea } from "@/components/ui/textarea";
import useTeamQueryModule from "@/hook/useTeamQueryModule";
import { Pencil, Trash2 } from "lucide-react";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import ProductListCard from "./ProductListCard";

function TeamInfo() {
  const { teamId } = useParams();
  const { oneTeam, addMember, deleteTeam, updateTeamInfo } = useTeamQueryModule(teamId);
  const [email, setEmail] = useState("");
  const [teamTitle, setTeamTitle] = useState("");
  const [teamInfo, setTeamInfo] = useState("");
  const [isEditing, setIsEditing] = useState(false);

  useEffect(() => {
    if (oneTeam) {
      setTeamTitle(oneTeam.title);
      setTeamInfo(oneTeam.info);
    }
    console.log(oneTeam);
  }, [oneTeam]);

  
  return (
    <div className="flex w-full max-h-full min-h-full gap-3 m-2 ">
      {!oneTeam?.personal && (
        <div className="flex flex-col w-1/3 h-full gap-3">
          <Card className="w-full border rounded shadow-md h-2/5">
            <CardHeader className="flex flex-row justify-between p-4 my-1">
              <CardTitle className="text-xl font-bold text-primary">팀</CardTitle>
              <div className="flex flex-row my-1">
                {!isEditing && <Pencil className="mx-2" onClick={() => setIsEditing(true)} />}
                <AlertDialog>
                  <AlertDialogTrigger>
                    <Trash2 />
                  </AlertDialogTrigger>
                  <AlertDialogContent>
                    <AlertDialogHeader>
                      <AlertDialogTitle>정말로 삭제하시겠습니까?</AlertDialogTitle>
                    </AlertDialogHeader>
                    <AlertDialogFooter>
                      <AlertDialogCancel className="border shadow-sm bg-secondary text-secondary-foreground hover:bg-secondary-accent">
                        취소
                      </AlertDialogCancel>
                      <AlertDialogAction onClick={() => deleteTeam(teamId)}>삭제</AlertDialogAction>
                    </AlertDialogFooter>
                  </AlertDialogContent>
                </AlertDialog>
              </div>
            </CardHeader>
            <CardContent className="flex flex-col justify-center gap-3 ">
              <div className="flex flex-row">
                <div className="mr-3 font-extrabold">팀 이름</div>
                <div>{teamTitle}</div>
              </div>
              {isEditing ? (
                <div>
                  <label htmlFor="teamInfo" className="mr-3">
                    팀 소개
                  </label>
                  <Textarea
                    id="teamInfo"
                    type="text"
                    value={teamInfo}
                    onChange={(e) => setTeamInfo(e.target.value)}
                  />
                  <Button
                    onClick={() => {
                      setIsEditing(false);
                      updateTeamInfo({
                        info: teamInfo,
                      });
                    }}
                  >
                    수정 완료
                  </Button>
                </div>
              ) : (
                <div className="flex flex-row">
                  <div className="mr-3 font-extrabold text-nowrap">팀 소개</div>
                  <div>{teamInfo}</div>
                </div>
              )}
            </CardContent>
          </Card>
          <Card className="flex flex-col w-full h-full border rounded shadow-md">
            <CardHeader className="p-4">
              <CardTitle className="text-xl font-bold text-primary">팀원</CardTitle>
            </CardHeader>
            <CardContent className="flex flex-col h-full ">
              <div className="flex flex-row items-center">
                <Input
                  value={email}
                  onChange={(e) => {
                    setEmail(e.target.value);
                  }}
                  type="email"
                  placeholder="팀원 이메일을 입력하세요"
                  className="my-4 mr-2"
                />
                <Button onClick={() => addMember(email)}>추가</Button>
              </div>
              <div className="w-full sm:h-24 md:h-64 lg:h-64 ">
                <ScrollArea className="w-full h-full border rounded-md">
                  <div className="p-4">
                    {oneTeam?.userResponseDtos.map((member, index) => (
                      <div key={index}>
                        <div className="text-sm">
                          <div className="flex items-center justify-between">
                            <div className="px-1 mx-1">
                              <Avatar>
                                <AvatarImage src={member.image} alt="@shadcn" />
                                <AvatarFallback>CN</AvatarFallback>
                              </Avatar>
                            </div>
                            <div className="flex flex-col items-start w-full text-sm font-bold">
                              <div className="mx-1 text-xs ">{member.nickname}</div>
                              <div className="mx-1 text-xs break-all text-zinc-600">
                                {member.email}
                              </div>
                            </div>
                          </div>
                        </div>
                        <Separator className="my-2" />
                      </div>
                    ))}
                  </div>
                </ScrollArea>
              </div>
            </CardContent>
          </Card>
        </div>
      )}
      <ProductListCard />
    </div>
  );
}

export default TeamInfo;
