using SnapTown.DTO;
using SnapTown.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Web;

namespace SnapTown.WebService.Converters
{
    public static class UserConverter
    {
        public static readonly Expression<Func<User, RegisterUserDto>> AsUserDto =
              x => new RegisterUserDto
              {
                  UserID = x.UserID,
                  Name = x.Name,
                  AuthToken = x.AuthToken,
                  GCMClientToken = x.GCMClientToken
              };

        public static readonly Expression<Func<RegisterUserDto, User>> AsUser =
             x => new User()
             {
                 UserID = x.UserID.Value,
                 Name = x.Name,
                 AuthToken = x.AuthToken,
                 GCMClientToken = x.GCMClientToken
             };
    }
}